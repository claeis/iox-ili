package ch.ehi.iox.objpool.impl.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class BTreeCursor<Key, Value>
{

	protected BTree<Key, Value> tree;
	protected Stack<NodeId> nodes=new Stack<NodeId>();
	protected Stack<Integer> entries=new Stack<Integer>();
	protected boolean beforeFirst;

	public BTreeCursor( BTree<Key, Value> btree )
	{
		this.tree = btree;
		initFirst(btree.getNode(btree.getRoot()),btree.height());
		entries.pop();
		entries.push(-1);
	}
    private void initFirst(Node h, int ht) {
    	nodes.push(h.getNodeId());
        if (ht == 0) {
        	// found first
        	entries.push(0);
        	return;
        }
        else {
        	entries.push(0);
        	initFirst(tree.getNode(h.getEntry(0).getChildNode()), ht-1);
        }
        return;
    }
    public Key getKey()
    {
    	return (Key)tree.getNode(nodes.peek()).getEntry(entries.peek()).getKey();
    }
    public Value getValue()
    {
    	return (Value) tree.getNode(nodes.peek()).getEntry(entries.peek()).getVal();
    }
    public void next(){
    	Node<Key,Value> currentNode=tree.getNode(nodes.peek());
    	int i=entries.peek();
    	if(i+1<currentNode.getEntryCount()){
    		i++;
    		entries.pop();
    		entries.push(i);
    		return;
    	}
    	int ht=0;
    	do{
        	nodes.pop();
        	entries.pop();
        	if(nodes.size()==0){
        		return; // end reached
        	}
        	currentNode=tree.getNode(nodes.peek());
        	i=entries.peek();
        	ht++;
    	}while(i+1==currentNode.getEntryCount());
		i++;
		entries.pop();
		entries.push(i);
    	initFirst(tree.getNode(currentNode.getEntry(i).getChildNode()), ht-1);
    }
    public boolean hasNext()
    {
    	if(nodes.size()==0){
    		return false;
    	}
    	Node<Key,Value> currentNode=tree.getNode(nodes.peek());
    	int i=entries.peek();
    	if(i+1<currentNode.getEntryCount()){
    		return true;
    	}
    	int ht=nodes.size()-1;
    	do{
    		ht--;
        	if(ht<0){
        		return false; // end reached
        	}
        	currentNode=tree.getNode(nodes.get(ht));
        	i=entries.get(ht);
    	}while(i+1==currentNode.getEntryCount());
    	return true;
    }
    
}
