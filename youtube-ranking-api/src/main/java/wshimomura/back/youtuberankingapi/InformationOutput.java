package wshimomura.back.youtuberankingapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

public class InformationOutput {

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
	static List<VideoResource> prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

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

				videoResource.setParameter(videoUrl, title, thumbnailUrl, comment, datetime);

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
