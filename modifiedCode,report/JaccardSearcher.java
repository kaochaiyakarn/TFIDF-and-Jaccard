//Name:  Chaiyakarn khanan // Chanwit panleng // Khachen Hempatawee
//Section:  2
//ID: 5988130 // 5988076 // 5988047

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class JaccardSearcher extends Searcher{

	public JaccardSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/
		
		/***********************************************/
	}

	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		List<String> singleWord;
		double sJt=0;  // score jaccard in pjo instruction;
		singleWord = tokenize(queryString);// is q
//		System.out.println(singleWord.size());
		//System.out.println(singleWord);
		//--------make it not same-------\\
		Set<String> set = new HashSet<>(singleWord);
		//------------------\\
		//list searchresult
		List<SearchResult> resultList = new ArrayList<SearchResult>(); //sorting
		//---------------Q
//		PriorityQueue<SearchResult> resultQ = new PriorityQueue<SearchResult>(); //
		//---------------Q
		for(Document doc : documents){
			Set<String> dForcal = new HashSet<>(doc.getTokens()); // is d
			Number2 inandUn = intersecandUn(set,dForcal);
			double scoreJac =0;
			scoreJac = inandUn.un!=0 ? 1.0*inandUn.intersec/inandUn.un :0; //is if()else
			SearchResult result = new SearchResult(doc, scoreJac); 
			resultList.add(result);	 //sorting
			//-------_Q
//			resultQ.add(result);
			//--------Q
		}
		Collections.sort(resultList);

		//-----------Q
//		List<SearchResult> finalResult = new ArrayList<>(resultQ);
		//-----------Q
		
		return resultList.subList(0,k);
//		return finalResult.subList(0,k); //for priority Q
		/***********************************************/
	}
	
	public static Number2 intersecandUn(Set<String> singleWord, Set<String> dForcal){
		Number2 ans = new Number2();
		ans.intersec = 0;
		ans.un = dForcal.size(); //start with size because union
		for(String term: singleWord){
			if(dForcal.contains(term)){ // d intersec with q
				ans.intersec++;
			}
			else{
				ans.un++; // d union with q
			}
		}
		return ans;
	}


	static class Number2{
		int intersec, un;
	}
}

