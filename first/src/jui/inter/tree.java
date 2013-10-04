
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.util.*;

public class tree {
	
	Document doc;

	public tree (String page) {
		try {
			File F = new File (page);
			doc = Jsoup.parse (F, "UTF-8", "");
			// just do the body for now
			Element body = doc.body ();
			String sofar = "";
			describe (sofar, body);
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

	void show (String s) {
		System.out.println (s);
	}

	void describe (String sofar, Element element) {
		try {
			String tag = element.tagName ();
			String id = element.id ();
			String out = id+":"+sofar+":"+tag;
			show (out);
			// if (element.hasText ()) show (element.text ());
			Elements elements = element.children ();
			if (elements.size () > 0) {
				int count = 0;
				for (Element child : elements) {
					String more = sofar+"."+tag+"-"+count;
					count++;
					describe (more, child);
				}
			}
			else {
				if (element.hasText ()) show (element.text ());
			}
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

	void finddata (String test) {
		try {
			StringTokenizer st = new StringTokenizer (test, ".-:");
			String first = st.nextToken ().toLowerCase ();
			Element node = null;
			if (first.equals ("body")) node = doc.body ();
			else if (first.equals ("head")) node = doc.head ();
			int num = Integer.parseInt (st.nextToken ());
			if (node == null) return;
			do {
				Elements children = node.children ();
				if (children.size () > num) {
					Element next = children.get (num);
					if (st.hasMoreTokens ()) {
						String nextag = st.nextToken ();
						if (st.hasMoreTokens ())
						num = Integer.parseInt (st.nextToken ());
						node = next;
					}
				}
				else break;
			} while (st.hasMoreTokens ());
			show ("Search for: "+test);
			if (node.hasText ()) show ("Result: "+node.text ());
			else show ("Node at "+test+" has no text");
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

	public static void main (String args []) {
		String input = "links.html";
		String test = ".body-0.center-0.table-0.tbody-0.tr-2.td-0:a";
		tree T = new tree (input);
		System.out.println ("Finding text");
		T.finddata (test);
	}

}

