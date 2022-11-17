import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TwoThreeTree {
    private Node root;

    TwoThreeTree() {
        root = new Node(2, null);
        root.left = new Node(1, root);
        root.center = new Node(2, root);
    }

    public void printTree() {
        System.out.println(pt(root));
    }

    private String pt(Node node) {
        if (node == null)
            return "";

        return node.printNode() + " L: (" + pt(node.left) + ") C: (" + pt(node.center) + ") R: (" + pt(node.right)
                + ")";
    }

    public enum Branch {
        left,
        center,
        right,
    }

    private void insertAll(ArrayList<Float> list) {
        list.forEach((value) -> insert(value));
    }

    /**
     * Inserts value into tree
     * 
     * @param value the value to insert
     */
    public void insert(Float value) {
        Node currentNode = root;
        Branch lastBranch = null;

        if (value == 0)
            return;

        while (!(currentNode.isLeaf())) {
            if (value < currentNode.value1) {
                currentNode = currentNode.left;
                lastBranch = Branch.left;
                continue;
            } else if (currentNode.value2 == 0 || value < currentNode.value2) {
                currentNode = currentNode.center;
                lastBranch = Branch.center;
                continue;
            } else {
                currentNode = currentNode.right;
                lastBranch = Branch.right;
                continue;
            }
        }

        if (currentNode.value1 == value) {
            System.out.println("Value already exists!");
            return;
        }

        currentNode = currentNode.parent;

        if (!(currentNode.isFull())) {
            if (lastBranch == Branch.center) {
                currentNode.right = new Node(value, currentNode);
                currentNode.value2 = value;
            } else if (lastBranch == Branch.left) {
                if (value > currentNode.left.value1) {
                    currentNode.right = new Node(currentNode.center.value1, currentNode);
                    currentNode.value2 = currentNode.value1;
                    currentNode.value1 = currentNode.center.value1 = value;
                } else {
                    currentNode.right = new Node(currentNode.center.value1, currentNode);
                    currentNode.value2 = currentNode.value1;
                    currentNode.center.value1 = currentNode.left.value1;
                    currentNode.left.value1 = currentNode.value1 = value;
                }
            } else {
                System.out.println("uncaught");
            }
        } else {
            sortNode(currentNode, value);

            // fix tree conditions
            if (currentNode == root) {
                moveUp(currentNode);
            } else if (!(currentNode.parent.isFull())) {
                // node must fuse with parent node
                moveUp(currentNode);
            } else {
                // parent node is full aswell
                boolean success = false;
                do {
                    success = moveUp(currentNode);
                    currentNode = currentNode.parent;
                } while (!success);
            }
        }
    }

    /**
     * 'Moves' the necessary sorting one node up (to the parent)
     * 
     * @param node
     * @return whether parent needs to be sorted aswell or not
     */
    private boolean moveUp(Node node) {
        boolean success = true;
        if (node == root) {
            node.left = new Node(node.value1, 0, node, node.left, node.center, null);
            node.left.left.parent = node.left;
            node.left.center.parent = node.left;

            node.center = new Node(node.tempValue, 0, node, node.right, node.tempNode, null);
            node.center.left.parent = node.center;
            node.center.center.parent = node.center;

            node.value1 = node.value2;
        } else if (node.parent.isFull()) {

            if (node.value2 >= node.parent.value2) {
                node.parent.tempValue = node.value2;

                node.parent.tempNode = new Node(node.tempValue, 0, node, node.right, node.tempNode, null); // unsure why
                                                                                                           // parent is
                                                                                                           // just
                                                                                                           // 'node'
                node.parent.tempNode.left.parent = node.parent.tempNode.center.parent = node.parent.tempNode;
            } else if (node.value2 >= node.parent.value1) {
                // shift to right
                node.parent.tempValue = node.parent.value2;
                node.parent.value2 = node.value2;
                node.parent.tempNode = node.parent.right;

                node.parent.right = new Node(node.tempValue, 0, node.parent, node.right, node.tempNode, null);
                node.parent.right.left.parent = node.parent.right.center.parent = node.parent.right;
            } else {
                // shift to right
                node.parent.tempValue = node.parent.value2;
                node.parent.tempNode = node.parent.right;
                node.parent.value2 = node.parent.value1;
                node.parent.right = node.parent.center;
                node.parent.value1 = node.value2;

                node.parent.center = new Node(node.tempValue, 0, node.parent, node.right, node.tempNode, null);
                node.parent.center.left.parent = node.parent.center.center.parent = node.parent.center;
            }

            // might need to sort node
            success = false;
        } else {
            if (node.value2 >= node.parent.value1) {
                node.parent.value2 = node.value2;

                node.parent.right = new Node(node.tempValue, 0, node.parent, node.right, node.tempNode, null);
                // node.right.parent = node.tempNode.parent = node.parent.right; old
                node.parent.right.left.parent = node.parent.right.center.parent = node.parent.right;

            } else {
                node.parent.value2 = node.parent.value1;
                node.parent.value1 = node.value2;

                node.parent.right = node.parent.center;
                node.parent.center = new Node(node.tempValue, 0, node.parent, node.right, node.tempNode, null);
                node.parent.center.left.parent = node.parent.center.center.parent = node.parent.center;
            }

        }
        // reset values and nodes, which are no longer needed
        node.value2 = node.tempValue = 0;
        node.right = node.tempNode = null;

        return success;
    }

    /**
     * Inserts the given value into given node and
     * sorts the given node to match all tree requirements
     * 
     * @param node  the node to be sorted
     * @param value the current value
     */
    private void sortNode(Node node, Float value) {

        if (value >= node.value2) {
            node.tempValue = value;
            node.tempNode = new Node(value, node);
            // tree condition has been broken
        } else if (value >= node.value1) {
            node.tempValue = node.value2;
            node.value2 = value;
            node.tempNode = node.right;
            if (node.center.value1 < value) {
                node.right = new Node(value, node);
            } else {
                node.right = node.center;
                node.center = new Node(value, node);
            }
        } else {
            node.tempValue = node.value2;
            node.value2 = node.value1;
            node.value1 = value;
            node.tempNode = node.right;
            node.right = node.center;
            if (node.left.value1 < value) {
                node.center = new Node(value, node);
            } else {
                node.center = node.left;
                node.left = new Node(value, node);
            }
        }
    }

    /**
     * Finds the next greater key/value
     * 
     * @param value
     * @return
     */
    public float search(Float value) {
        return ((Node) searchNode(value).get("node")).value1;
    }

    /**
     * Finds the node holding the next greater key
     * 
     * @param value the value to look for
     * @return the node with the next greater key and its last branch
     */
    private Map<String, Object> searchNode(Float value) {
        Node currentNode = root;
        Branch lastBranch = null;
        while (!(currentNode.isLeaf())) {
            if (value < currentNode.value1) {
                if (currentNode.left == null) {
                    break;
                }
                currentNode = currentNode.left;
                lastBranch = Branch.left;
                continue;
            } else if (currentNode.value2 == 0 || value < currentNode.value2) {
                if (currentNode.center == null) {
                    break;
                }
                currentNode = currentNode.center;
                lastBranch = Branch.center;
                continue;
            } else {
                if (currentNode.right == null) {
                    break;
                }
                currentNode = currentNode.right;
                lastBranch = Branch.right;
                continue;
            }
        }
        Map<String, Object> l = new HashMap<String, Object>();

        l.put("node", (Node) currentNode);
        l.put("branch", lastBranch);

        return l;
    }

    /**
     * Deletes the value
     * 
     * @param value the value to be deleted
     */
    public void delete(Float value) {
        var result = searchNode(value);
        var foundNode = (Node) result.get("node");
        if (foundNode.value1 != value) {
            return;
        }
        delete(result);
    }

    private void delete(Map<String, Object> result) {
        var node = (Node) result.get("node");
        var branch = (Branch) result.get("branch");
        var numChilds = node.parent.getNumChilds();

        if (numChilds == 3) {
            node.parent.setBranchedNode(branch, null);
            fixCorrespondingParent(node.parent);
            return;
        }

        if (numChilds < 3) {
            var originalNodeValue = node.value1;
            var nodeValues = new HashSet<Float>();
            Branch lastBranch;
            do {
                nodeValues.addAll(node.getChildValues());
                lastBranch = node.parent.getChildBranch(node);
                node = node.parent;
            } while (node.getNumChilds() != 3 && node.parent != null);

            node.setBranchedNode(lastBranch, null);

            fixCorrespondingParent(node);

            if (node.parent == null) {
                switch (lastBranch) {
                    case left:
                        node.setBranchedNode(lastBranch, new Node(1, node));
                        break;
                    case center:
                        node.setBranchedNode(lastBranch, new Node(2, node));
                        break;
                    default:
                        break;
                }
            }
            nodeValues.remove(originalNodeValue);
            insertAll(new ArrayList<Float>(nodeValues));
        }

    }

    /**
     * Precondition: node had three children before deletion
     * 
     * @param node
     */
    private void fixCorrespondingParent(Node node) {
        var values = node.getChildValues();
        if (values.size() == 2) {
            node.value1 = Math.max(values.get(0), values.get(1));
        } else {
            node.value1 = values.get(0);
        }
        node.value2 = 0;

        if (node.right == null)
            return;
        if (node.left == null)
            node.left = node.center;

        node.center = node.right;
        node.right = null;
    }

}
