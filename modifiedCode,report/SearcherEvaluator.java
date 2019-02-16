//Name:  Chaiyakarn khanan // Chanwit panleng // Khachen Hempatawee
//Section:  2
//ID: 5988130 // 5988076 // 5988047

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

public class SearcherEvaluator {
	private List<Document> queries = null;				//List of test queries. Each query can be treated as a Document object.
	private  Map<Integer, Set<Integer>> answers = null;	//Mapping between query ID and a set of relevant document IDs
	
	public List<Document> getQueries() {
		return queries;
	}

	public Map<Integer, Set<Integer>> getAnswers() {
		return answers;
	}

	/**
	 * Load queries into "queries"
	 * Load corresponding documents into "answers"
	 * Other initialization, depending on your design.
	 * @param corpus
	 */
	public SearcherEvaluator(String corpus)
	{
		String queryFilename = corpus+"/queries.txt";
		String answerFilename = corpus+"/relevance.txt";
		
		//load queries. Treat each query as a document. 
		this.queries = Searcher.parseDocumentFromFile(queryFilename);
		this.answers = new HashMap<Integer, Set<Integer>>();
		//load answers
		try {
			List<String> lines = FileUtils.readLines(new File(answerFilename), "UTF-8");
			for(String line: lines)
			{
				line = line.trim();
				if(line.isEmpty()) continue;
				String[] parts = line.split("\\t");
				Integer qid = Integer.parseInt(parts[0]);
				String[] docIDs = parts[1].trim().split("\\s+");
				Set<Integer> relDocIDs = new HashSet<Integer>();
				for(String docID: docIDs)
				{
					relDocIDs.add(Integer.parseInt(docID));
				}
				this.answers.put(qid, relDocIDs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns an array of 3 numbers: precision, recall, F1, computed from the top *k* search results 
	 * returned from *searcher* for *query*
	 * @param query
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getQueryPRF(Document query, Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		List<SearchResult> results = searcher.search(query.getRawText(), k);
		
		Set<Integer> docID = new TreeSet<Integer>();
		for(SearchResult result : results){
			docID.add(result.getDocument().getId());		
		}
		
		Set<Integer> rerl = answers.get(query.getId());
		int R = docID.size();
		int G = rerl.size();
//		Set<Integer> intersec = new TreeSet<Integer>();
		int count=0;
		for(Integer i : docID){
			if(rerl.contains(i)){
				count++;
			}
		}
		int newSize = count;
		double P = 1.0*newSize/R;
		double recall = 1.0*newSize/G;
		
		double f1 = (2*P*recall)/ (P+recall); //NAN will be show
		double realf1 = (P+recall)!=0 ?  (2*P*recall)/ (P+recall):0;
		double[] anss = {P,recall,realf1};
		
		
		return anss; //check
		/****************************************************************/
	}
	
	/**
	 * Test all the queries in *queries*, from the top *k* search results returned by *searcher*
	 * and take the average of the precision, recall, and F1. 
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getAveragePRF(Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		double avg[] ={0,0,0};
		double[] ans;
		int count=0;
		int j=0;
		for(Document q: getQueries()){
			ans = getQueryPRF(q, searcher, k);
			for(int i=0;i<3;i++){
				avg[i] += ans[i];
			}
			count++;		
		}
		for(int i=0;i<3;i++){
			avg[i] /=count;
		}
		return avg;
		/****************************************************************/
	}
}
