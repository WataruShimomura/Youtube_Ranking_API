package wshimomura.back.youtuberankingapi;

import java.io.Serializable;

public class VideoResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private String videoUrl;
	private String title;
	private String thumbnail;
//	private String comment;
//	private String date;

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

//	public String getComment() {
//		return comment;
//	}
//
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//
//	public String getDate() {
//		return date;
//	}
//
//	public void setDate(String date) {
//		this.date = date;
//	}

	public VideoResource() {
		super();
	}

	public void setParameter(String videoUrl, String title, String thumbnail) {
		this.videoUrl = videoUrl;
		this.title = title;
		this.thumbnail = thumbnail;
//		this.comment = comment;
//		this.date = date;
	}

}
