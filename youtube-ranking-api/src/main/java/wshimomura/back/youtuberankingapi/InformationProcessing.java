package wshimomura.back.youtuberankingapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
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
	 * 検索ワード = query
	 * 検索期間フィルター = period
	 *
	 *検索条件を指定後、youtube.searchメソッドにより情報をセットし、
	 *検索結果をsearchResultListリストに格納し、prettyPrintを呼び出す。
	 */
	@GetMapping("/test")
	public static List<VideoResource> main(@RequestParam("query") String query, @RequestParam("date") String date) throws IOException {
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("youtube-cmdline-search-sample").build();

		DateTime period = DataInput.getInputDate(date);

		YouTube.Search.List search = youtube.search().list("id,snippet");

		//APIキー（注意：gitにpullする際は情報を空にしておくこと）
		search.setKey("AIzaSyAro_oITBRyLK7keDUe-4SBaFept9u8UkM");
		//検索キーワード
		search.setQ(query);
		//検索対象を動画のみを指定（プレイリストやチャンネルは除外する）
		search.setType("video");
		//検索対象を再生回数の高い順に表示させる
		search.setOrder("viewCount");
		//検索期間を指定
		search.setPublishedAfter(period);
		//検索する動画の情報を指定
//		search.setFields(
//				"items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url/description,snippet)");
		//表示されるされる動画の数を指定（現在２５）
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
		SearchListResponse searchResponse = search.execute();

		List<SearchResult> searchResultList = searchResponse.getItems();

		List<VideoResource> result = prettyPrint(searchResultList.iterator(), query);

		return result;

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
	private static List<VideoResource> prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		List<VideoResource> resultVideoResource = new ArrayList<>();


		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Double checks the kind is video.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

				VideoResource videoResource = new VideoResource();

				String videoUrl = "https://www.youtube.com/watch?v=" + rId.getVideoId();

				String title = singleVideo.getSnippet().getTitle();

				String thumbnailUrl = thumbnail.getUrl();

				String comment = singleVideo.getSnippet().getDescription();

				DateTime dt = singleVideo.getSnippet().getPublishedAt();
				String datetime = dt.toString();

				videoResource.setParameter(videoUrl,title,thumbnailUrl,comment,datetime);

				resultVideoResource.add(videoResource);

				//動画のURL
				System.out.println(" Video Id:https://www.youtube.com/watch?v=" + rId.getVideoId());
				//動画のタイトル
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				//動画のサムネイルURL
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				//動画のコメント
				System.out.println(" Comment: " + singleVideo.getSnippet().getDescription());
				//動画の投稿日（出力する際はDatetimeクラス）
				System.out.println(" Comment: " + singleVideo.getSnippet().getPublishedAt());
				System.out.println(
						"\n-------------------------------------------------------------\n");
			}

		}
		return resultVideoResource;
	}
}
