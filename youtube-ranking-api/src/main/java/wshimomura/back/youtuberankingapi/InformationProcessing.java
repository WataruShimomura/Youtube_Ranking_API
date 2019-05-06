package wshimomura.back.youtuberankingapi;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

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
	private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

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
	@CrossOrigin
	@GetMapping("/test")
	public static List<VideoResource> main(@RequestParam("query") String query, @RequestParam("date") String date)
			throws IOException {
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("youtube-cmdline-search-sample").build();

		DateTime period = DataInput.getInputDate(date);

		YouTube.Search.List search = youtube.search().list("id,snippet");

		//APIキー（注意：gitにpullする際は情報を空にしておくこと）
		search.setKey("");
		//検索キーワード
		search.setQ(query);
		//検索対象を動画のみを指定（プレイリストやチャンネルは除外する）
		search.setType("video");
		//検索対象を再生回数の高い順に表示させる
		search.setOrder("viewCount");
		//検索期間を指定
		search.setPublishedAfter(period);
		//検索する動画の情報を指定
//				search.setFields(
//						"items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url/description,snippet)");
		//表示されるされる動画の数を指定（現在10）
		search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
		SearchListResponse searchResponse = search.execute();

		List<SearchResult> searchResultList = searchResponse.getItems();

		List<VideoResource> result = InformationOutput.prettyPrint(searchResultList.iterator(), query);

		return result;

	}

}
