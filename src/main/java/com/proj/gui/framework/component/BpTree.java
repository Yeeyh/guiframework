package com.proj.gui.framework.component;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.tools.bp.crypt.MD5;

public class BpTree extends JTree {
	private static final long serialVersionUID = 1L;
	private DefaultTreeModel model;
	private TreeSelectionModel selectModel;
	private DefaultMutableTreeNode root;
	private Set<String> nodes = new HashSet<String>();
	// 由于每次select会触发select事件，这里改成缓存模式，只有真正调用flush函数才会出发select事件
	private DefaultMutableTreeNode selectNode;
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	public boolean isRoot() {
		DefaultMutableTreeNode node = select();
		if(node==null) {
			return false;
		}
		if(!root.getUserObject().equals(node.getUserObject())) {
			return false;
		}
		return true;
	}
	
	public boolean isRootChild() {
		DefaultMutableTreeNode node = select();
		if(node==null) {
			return false;
		}
		TreeNode parent = node.getParent();
		if(parent==null) {
			return false;
		}
		if(!parent.toString().equals("")) {
			return false;
		}
		return true;
	}

	public BpTree() {
		setShowsRootHandles(true);
		setRootVisible(false);
		root = new DefaultMutableTreeNode("");

		model = (DefaultTreeModel) getModel();
		model.setRoot(root);// 先初始化根节点，不初始化会显示更多的组件自带内容

		selectModel = getSelectionModel();
		selectModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		selectModel.setSelectionPath(new TreePath(root.getPath()));
	}

	public BpTree root(Object r) {
		setVisible(false);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(r);
		model.insertNodeInto(node, root, root.getChildCount());
		removeExists(node);
		select(node);
		setVisible(true);
		return this;
	}

	public BpTree branch(Object... paths) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(false);
				DefaultMutableTreeNode node = select();
				for (Object path : paths) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(path);
					model.insertNodeInto(child, node, node.getChildCount());
					removeExists(child);
					node = child;
				}
				select(expand(node));
				flush();
				setVisible(true);
		}});
		return this;
	}

	public BpTree leaf(Object[] leafs) {
		DefaultMutableTreeNode node = select();
		return leaf(node, leafs);
	}
	@SuppressWarnings("unchecked")
	public BpTree leaf(DefaultMutableTreeNode node, Object... leafs) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(false);
				Map<String,DefaultMutableTreeNode> childs = new HashMap<String,DefaultMutableTreeNode>();
				for (Enumeration<DefaultMutableTreeNode> e = node.children(); e.hasMoreElements();) {
					DefaultMutableTreeNode child = e.nextElement();
					String md5 = MD5.bit32(Arrays.toString(child.getUserObjectPath()));
					childs.put(md5,child);
				}
				Set<String> newChilds = new HashSet<String>();
				for (Object leaf : leafs) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(leaf);
					model.insertNodeInto(child, node, node.getChildCount());
					newChilds.add(MD5.bit32(Arrays.toString(child.getUserObjectPath())));
					removeExists(child);
				}
				//删除不存在的节点
				Set<String> keySet = childs.keySet();
				keySet.removeAll(newChilds);;
				for(String md5 : keySet) {
					model.removeNodeFromParent(childs.get(md5));
				}
				expand(node);
				flush();
				setVisible(true);
			}});
		return this;
	}

	// 格式  xx/xx/xx/xx
	public BpTree select(String names) {
		if(names.charAt(0)=='/') {
			names = names.substring(1, names.length());
		}
		String[] path = names.replaceAll("\\\\", "/").split("/");
		DefaultMutableTreeNode node = searchNode(path);
		if (node != null) {
			select(node);
		}
		return this;
	}

	public BpTree select(DefaultMutableTreeNode node) {
		selectNode = node;
		return this;
	}

	public BpTree select(TreePath treePath) {
		selectModel.setSelectionPath(treePath);
		selectNode = null;
		return this;
	}
	
	public DefaultMutableTreeNode select() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selectNode == null ? getLastSelectedPathComponent()
				: selectNode);
		return node;
	}

	public void flush() {
		if (selectNode == null) {
			return;
		}
		select(new TreePath(selectNode.getPath()));
	}
	public void flush(String names) {
		select(names);
		flush();
	}
	
	public void remove(String names) {
		select(names);
		model.removeNodeFromParent(selectNode);
	}

	private void removeExists(DefaultMutableTreeNode node) {
		String md5 = MD5.bit32(Arrays.toString(node.getUserObjectPath()));
		if (nodes.contains(md5)) {
			model.removeNodeFromParent(node);
			return;
		}
		nodes.add(md5);
	}

	private TreePath expand(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		expand(path);
		return path;
	}

	private void expand(TreePath path) {
		expandPath(path);
//		try {
//			fireTreeWillExpand(path);
//		} catch (ExpandVetoException e) {
//			e.printStackTrace();
//		}
	}

	private DefaultMutableTreeNode searchNode(String... names) {
		if (names == null || names.length == 0)
			return null;
		String fetchName = names[names.length - 1];
		names = Arrays.copyOfRange(names, 0, names.length - 1);
		DefaultMutableTreeNode node = root;
		for (String name : names) {
			node = searchNode(node, name);
		}
		return searchNode(node, fetchName);
	}

	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode searchNode(DefaultMutableTreeNode root, String name) {
		if (root == null)
			return null;
		for (Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			if (node.equals(root))
				continue;
			if (!name.equalsIgnoreCase(node.getUserObject().toString()))
				continue;
			return node;
		}
		return null;
	}
}
