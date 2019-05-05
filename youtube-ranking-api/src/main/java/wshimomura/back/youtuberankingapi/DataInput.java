package wshimomura.back.youtuberankingapi;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.google.api.client.util.DateTime;

/**
 * @author 下村航
 * 動画のリクエスト情報をapi用に処理し値を返すクラス
 *
 */
public class DataInput {

	/**
	 * 検索期間を
	 * ・一日
	 * ・一週間
	 * ・一か月
	 * の３種類で指定を行い、その期間を検索期間とし値を返すメソッド
	 *
	 * @return dateTimeSetRtu
	 * 検索期間の始まりの時間の情報を持ったDateTimeクラス
	 *
	 * @throws IOException
	 */
	public static DateTime getInputDate(String dateQuery) throws IOException {
		String inputQuery = dateQuery;
		Date date = new Date();
		Calendar time = Calendar.getInstance();
		time.setTime(date);

		if (inputQuery == "1") {

			time.add(Calendar.DAY_OF_MONTH, -1);

			Date d1 = time.getTime();

			DateTime dateTimeSetRtu = new DateTime(d1);

			return dateTimeSetRtu;

		} else {
			if (inputQuery == "2") {

				time.add(Calendar.WEEK_OF_MONTH, -1);

				Date d1 = time.getTime();

				DateTime dateTimeSetRtu = new DateTime(d1);

				return dateTimeSetRtu;
			} else {

				time.add(Calendar.MONTH, -1);

				Date d1 = time.getTime();

				DateTime dateTimeSetRtu = new DateTime(d1);

				return dateTimeSetRtu;

			}

		}

	}

}
