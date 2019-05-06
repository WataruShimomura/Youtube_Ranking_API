import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DuplicateSearch {

	public static void main(String[] args) {
		String test = "foob";
		duplicateSearch(test);
	}

	public static void duplicateSearch(String str) {
		String[] duplicate = str.split("");

		System.out.println(Arrays.asList(duplicate));

		List<String> list = Arrays.asList(duplicate);
		List<Integer> result = new ArrayList<Integer>();

		for(int i = 0 ; i < list.size() ; i++ ) {
			Integer math = 0;
			

			result.add(math);
		}
		System.out.println(Collections.max(result));
	}

}
