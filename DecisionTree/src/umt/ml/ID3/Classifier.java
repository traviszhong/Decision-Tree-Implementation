package umt.ml.ID3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Feb 28, 2016
 */
public class Classifier {
	private int[][] trainingData;
	private int[] trainingClass;
	private String dataPath;
	private DecisionTreeNode root;
	/**
	 * Constructor
	 * @param path: path of the .csv file
	 * @throws Exception 
	 */
	public Classifier(String path) throws Exception{
		this.dataPath=path;
		trainingData=new int[629][9];
		trainingClass=new int[629];
		List<Integer> dim=new ArrayList<Integer>();
		List<Integer> sample=new ArrayList<Integer>();
		for(int i=0;i<629;i++) sample.add(i);
		for(int i=0;i<9;i++) dim.add(i);
		this.root=new DecisionTreeNode(dim,sample);
		this.loadData();
		buildDecisionTree(this.root);
	}
	/**
	 * 
	 * @return the a string of level order traversal of the decision tree
	 */
	public String bfsTraversal(){
		StringBuilder str=new StringBuilder();
		//use a queue to do the breadth first searsh
		Queue<Qnode> q=new LinkedList<Qnode>();
		q.offer(new Qnode(0,this.root));
		while(!q.isEmpty()){
			int size=q.size();
			for(int i=0;i<size;i++){
				Qnode qn=q.poll();
				DecisionTreeNode d=qn.n;
				str.append(d.dimId+":"+qn.parent+",");
				for(DecisionTreeNode dt:d.next.values()){
					q.offer(new Qnode(i,dt));
				}
			}
			str.deleteCharAt(str.length()-1);
			//use a "|" to separate levels
			str.append("|");
		}
		return str.toString();
	}
	/**
	 * 
	 * @param test: a list of integer which represent the id of samples
	 * @return: the dimension with best information gain fo the current sample set
	 */
	public int getClassLabel(List<Integer> test){
		DecisionTreeNode p=this.root;
		while(p.classLable==2){
			if(p.next.containsKey(test.get(p.dimId)))
				p=p.next.get(test.get(p.dimId));
			else return 1;
		}
		return p.classLable;
	}
	/**
	 * read in the data from the csv file, store them in a 2d array
	 * @throws Exception
	 */
	public void loadData() throws Exception{
		File csvData= new File(this.dataPath);
		BufferedReader br=new BufferedReader(new FileReader(csvData));
		String line="";
		int index=-1;
		while((line=br.readLine())!=null){
			if(index==-1){
				index++;continue;
			}
			StringTokenizer st=new StringTokenizer(line,",");
			for(int i=0;i<=8;i++){
				trainingData[index][i]=Integer.valueOf(st.nextToken());
			}
			trainingClass[index]=Integer.parseInt(st.nextToken());
			index++;
		}
	}
	/**
	 * build the decision tree in an recursive way
	 * @param root
	 */
	public void buildDecisionTree(DecisionTreeNode root){
		if(isLeaf(root)) return;
		int dimension=getBestDim(root.sampleList,root.dimList);
		root.dimId=dimension;
		root.dimList.remove((Object)dimension);
		Map<Integer,List<Integer>> hash=new HashMap<Integer,List<Integer>>();
		for(int i:root.sampleList){
			if(!hash.containsKey(this.trainingData[i][dimension])) hash.put(this.trainingData[i][dimension],new ArrayList<Integer>());
			hash.get(this.trainingData[i][dimension]).add(i);
		}
		for(Map.Entry<Integer,List<Integer>> e:hash.entrySet()){
			root.next.put(e.getKey(),new DecisionTreeNode(new ArrayList<Integer>(root.dimList),new ArrayList<Integer>(e.getValue())));
		}
		//recursion to build the decision tree
		for(DecisionTreeNode d:root.next.values()){
			buildDecisionTree(d);
		}
	}
	/**
	 * find out whether a node is a leaf or not
	 * @param root
	 * @return
	 */
	public boolean isLeaf(DecisionTreeNode root){
		Set<Integer> set=new HashSet<Integer>();
		for(int i:root.sampleList){
			root.classLable=this.trainingClass[i];
			set.add(root.classLable);
		}
		if(set.size()==1) return true;
		root.classLable=2;
		return false;
	}
	/**
	 * Find a dimesion with best information gain by using greedy approach
	 * @param sample: current data set
	 * @param dim: a list of available dimensions to choose from
	 * @return
	 */
	public int getBestDim(List<Integer> sample,List<Integer> dim){
		int rst=-1;
		double maxInfoGain=Double.MIN_VALUE;
		for(int i:dim){
			double infoGain=getInfoGain(sample,i);
			if(maxInfoGain<infoGain){
				maxInfoGain=infoGain;
				rst=i;
			}
		}
		return rst;
	}
	/**
	 * the function to calculate the entropy for a specific sample set
	 * @param data: the sample set
	 * @return
	 */
	public double getEntropy(List<Integer> data){
		int total=data.size();
		double entropy=0;
		Map<Integer,Integer> hash=new HashMap<Integer,Integer>();
		for(int d:data){
			if(hash.containsKey(d)) hash.put(d,hash.get(d)+1);
			else hash.put(d,1);
		}
		for(int v:hash.values()){
			double p=((double)v)/total;
			entropy-=p*(Math.log(p)/Math.log(2));
		}
		return entropy;
	}
	/**
	 * the function to calculate the information gain fo the dimension provided
	 * @param data: sample set
	 * @param attr: a selected dimension 
	 * @return: the information gain
	 */
	public double getInfoGain(List<Integer> data,int attr){
		List<Integer> prev=new ArrayList<Integer>();
		Map<Integer,List<Integer>> hash=new HashMap<Integer,List<Integer>>();
		for(int d:data){
			prev.add(trainingClass[d]);
			if(hash.containsKey(trainingData[d][attr])) hash.get(trainingData[d][attr]).add(trainingClass[d]);
			else{
				hash.put(trainingData[d][attr],new ArrayList<Integer>());
				hash.get(trainingData[d][attr]).add(trainingClass[d]);
			}
		}
		double before=getEntropy(prev);
		double after=0;
		for(List<Integer> l:hash.values()){
			double weight=((double)(l.size()))/data.size();
			after+=weight*getEntropy(l);
		}
		return before-after;
	}
}
