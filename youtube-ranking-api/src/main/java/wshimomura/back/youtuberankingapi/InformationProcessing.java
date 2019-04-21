package wshimomura.back.youtuberankingapi;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

/**
 * @author 下村航
 * 入力情報を処理し、値をもとに動画の情報を出力するクラス
 *
 */
public class InformationProcessing {

	/**.HTTPトランスポートのグローバルインスタンス */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/**.JSONファクトリのグローバルインスタンス */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**返される動画の最大数のグローバルインスタンス */
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	/**すべてのAPIリクエストを行うためのYoutubeオブジェクトのグローバルインスタンス。 */
	private static YouTube youtube;

	/**
	 * 情報を入力し、入力した情報をもとにリクエストを送り、情報を出力するメソッドを呼び出すメソッド。
	 *
	 * ～入力情報～
	 * 検索ワード = queryTerm
	 * 検索期間フィルター = period
	 *
	 *検索条件を指定後、youtube.searchメソッドにより情報をセットし、
	 *検索結果をsearchResultListリストに格納し、prettyPrintを呼び出す。
	 */
	public static void main(String args[]) throws IOException {
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("youtube-cmdline-search-sample").build();
		String queryTerm = DataInput.getInputQuery();

		DateTime period = DataInput.getInputDate();

		YouTube.Search.List search = youtube.search().list("id,snippet");

		//APIキー（注意：gitにpullする際は情報を空にしておくこと）
		search.setKey("");
		//検索キーワード
		search.setQ(queryTerm);
		//検索対象を動画のみを指定（プレイリストやチャンネルは除外する）
		search.setType("video");
		//検索期間を指定
		search.setPublishedAfter(period);
		//検索する動画の情報を指定
		search.setFields(
				"items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		//表示されるされる動画の数を指定（現在２５）
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
		SearchListResponse searchResponse = search.execute();

		List<SearchResult> searchResultList = searchResponse.getItems();

		prettyPrint(searchResultList.iterator(), queryTerm);

	}

	/**
	 * リクエストされた情報を出力するメソッド。
	 *
	 * ～出力情報～
	 * 動画URL = rId.getVideoId()
	 * 動画タイトル = singleVideo.getSnippet().getTitle()
	 * サムネイルURL = thumbnail.getUrl()
	 *
	 * @param iteratorSearchResults
	 * 検索結果をリストにしたもの、search.setFieldsで指定した情報が格納されている。
	 *
	 * @param query
	 * 検索ワード
	 *
	 */
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \""
				+ query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Double checks the kind is video.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

				//動画のURL
				System.out.println(" Video Id:https://www.youtube.com/watch?v=" + rId.getVideoId());
				//動画のタイトル
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				//動画のサムネイルURL
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				System.out.println(
						"\n-------------------------------------------------------------\n");
			}
		}
	}
}