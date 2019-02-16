//Name:  Chaiyakarn khanan // Chanwit panleng // Khachen Hempatawee
//Section:  2
//ID: 5988130 // 5988076 // 5988047

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class TFIDFSearcher extends Searcher {
	Set<String> setV = new TreeSet<String>(); // treeset fastermthan hash set
												// because it didnt have empty;
	Set<String> answerSet = new HashSet<String>();
	// =========create map to store answer========//
	Map<String, List<Integer>> prepareCache = new HashMap(); // count how many
																// cat in doc
																// not how many
																// cat in one
																// doc
	// ===========================================//

	public TFIDFSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/
		for (Document doc : documents) {
			for (String str : doc.getTokens()) {
				if (!prepareCache.containsKey(str)) {
					prepareCache.put(str, new ArrayList<>());
				}
				if (!prepareCache.get(str).contains(doc.getId())) {
					prepareCache.get(str).add(doc.getId()); // anti same list;
				}
				if (!setV.contains(str)) {
					setV.add(str);
				}
			}
		}
		for (String str : prepareCache.keySet()) {
			termCache.put(str, prepareCache.get(str).size());
		}
		/***********************************************/
	}

	Map<String, Integer> termCache = new HashMap<>();

	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		List<String> terms;
		terms = tokenize(queryString);// is x
		List<SearchResult> resultList = new ArrayList<SearchResult>(); // sorting
		// ---------------Q
		PriorityQueue<SearchResult> resultQ = new PriorityQueue<SearchResult>(); // faster
																					// //Q
		// ---------------Q
		for (Document doc : documents) {
			double simila = 0.0;
			double dotP = 0.0, mag1 = 0.0, mag2 = 0.0;

			Set<String> un = new TreeSet<>();

			for (String str : doc.getTokens()) {
				un.add(str);
			}
			// Set<String> un1 = new TreeSet<>();

			for (String str : terms) {
				un.add(str);
			}
			double weight, weight1;

			for (String str : un) {
				weight = 0;
				weight1 = 0;
				if (doc.getTokens().contains(str)) {
					weight = calW(str, doc.getTokens(), documents);
				}
				if (terms.contains(str)) {
					weight1 = calW(str, terms, documents);
				}
				dotP += weight * weight1;
				mag1 += weight * weight;
				mag2 += weight1 * weight1;

			}
			simila = mag1 == 0 || mag2 == 0 ? Double.NaN : dotP / (Math.sqrt(mag1) * Math.sqrt(mag2));
			resultList.add(new SearchResult(doc, simila));

			// if(doc.getId()%50 ==0){
			// System.out.println(doc.getId());
			// }

		}

		Collections.sort(resultList);

		return resultList.subList(0, k);
		// return new ArrayList<>(resultQ).subList(0,k); // if bug do like
		// jaccard on Q version;
		/***********************************************/
	}
	// creATE NEW METHOD; //test to put in same method // avoid confuse

	int documentF(String term, List<Document> docID) {
		int count = 0;
		if (termCache.containsKey(term)) {
			return termCache.get(term);
		}
		for (Document i : docID) {

			if (i.getTokens().contains(term)) {
				count++;
			}
		}
		termCache.put(term,count);
		return count;
		// return
		// docID.stream().filter(doc->doc.getTokens().contains(t)).toArray().length;
		// Advance instead loop i;
	}

	Double idf(String term, List<Document> docs) {
		double val = Math.log10(1 + 1.0 * docs.size() / documentF(term, docs)); // try
																				// to
																				// catch
																				// to
																				// double
																				// instead
																				// this
																				// .

		return val;
	}

	int termFreq(String t, List<String> xString) {
		int count = 0;
		for (String singleStr : xString) {
			if (t.equals(singleStr)) {
				count++;
			}
		}
		return count;
		// return Collections.frequency(xString,t);
	}

	double fullCalTermFreq(String term, List<String> xString) {

		// if(xString.contains(term)){
		// return 1+ Math.log10(termFreq(term,xString));
		// }
		// else{
		// return 0.0;
		// }
		return xString.contains(term) ? 1 + Math.log10(termFreq(term, xString)) : 0;

		// return 0.0;
	}

	double calW(String term, List<String> xString, List<Document> docs) {

		return fullCalTermFreq(term, xString) * idf(term, docs);
	}

	double cosine(List<Double> x, List<Double> y) {
		double dotP = 0.0, mag1 = 0.0, mag2 = 0.0;
		for (int i = 0; i < x.size(); i++) {
			dotP += x.get(i) * y.get(i);
			mag1 += Math.pow(x.get(i), 2);
			mag2 += Math.pow(y.get(i), 2);

		}
		double Tmag1 = Math.sqrt(mag1);
		double Tmag2 = Math.sqrt(mag2);
		if (mag1 != 0.0 && mag2 != 0.0) {
			return dotP / (Tmag1 * Tmag2);
		} else {
			return Double.NaN;
		}
	}

}
