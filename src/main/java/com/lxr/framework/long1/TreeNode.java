package com.lxr.framework.long1;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    public T t;
    private TreeNode<T> parent;
    
    public List<TreeNode<T>> nodelist;
    
    public TreeNode(T stype){
        t = stype;
        parent = null;
        nodelist = new ArrayList<TreeNode<T>>();
    }

    public TreeNode<T> getParent() {
        return parent;
    }

	public void add(TreeNode<T> t) {
		nodelist.add(t);

	}

	public List<TreeNode<T>> getNodelist() {
		return nodelist;
	}

	public void setNodelist(List<TreeNode<T>> nodelist) {
		this.nodelist = nodelist;
	}

	@Override
	public String toString() {
		return "TreeNode [t=" + t + ", parent=" + parent + ", nodelist="
				+ nodelist + "]";
	}
    
	
	
    
}