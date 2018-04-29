package com.lxr.framework.long1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class  TreeBuilder<T> {
	
	
	public List<TreeNode<T>> generate() {
		
		List<TreeNode<T>> rootList = new ArrayList<TreeNode<T>>();
		
		List<TreeNode> addlist = new ArrayList<TreeNode>();
		
		rootList.addAll(getRoots());
		addlist.addAll(rootList);
		
		while(true){
			List<TreeNode> wlist = new ArrayList<TreeNode>();
			for (TreeNode node : addlist) {
				node.setNodelist(getChilds(node));
				wlist.addAll(node.getNodelist());
			}
			
			addlist = wlist;
			
			if(addlist.size()<1)break;
			
			
		}
		
		
		
		return rootList;
	}
	
	public abstract List<TreeNode<T>> getRoots();
	
	public abstract List<TreeNode<T>> getChilds(TreeNode<T> node);



	
	
	
	
	
	

}
