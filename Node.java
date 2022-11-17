import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    public float value1, value2, tempValue;
    public Node parent, left, center, right, tempNode;

    Node(float value, Node parent) {
        this.value1 = value;
        this.left = this.center = this.right = null;
        this.parent = parent;
    }

    Node(float value1, float value2, Node parent) {
        this.value1 = value1;
        this.value2 = value2;
        this.left = this.center = this.right = null;
        this.parent = parent;
    }

    Node(float value1, float value2, Node parent, Node left, Node center, Node right) {
        this.value1 = value1;
        this.value2 = value2;
        this.parent = parent;
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public boolean isLeaf() {
        return this.left == null && this.center == null && this.right == null;
    }

    public boolean isFull() {
        return this.left != null && this.center != null && this.right != null;
    }

    public int getNumChilds() {
        var counter = 0;
        if (this.center != null) {
            counter++;
        }
        if (this.left != null) {
            counter++;
        }
        if (this.right != null) {
            counter++;
        }
        return counter;
    }

    public void setBranchedNode(TwoThreeTree.Branch branch, Node node) {
        switch (branch) {
            case center:
                this.center = node;
                break;
            case left:
                this.left = node;
                break;
            case right:
                this.right = node;
                break;
        }
    }

    public ArrayList<Float> getChildValues() {
        var values = new HashSet<Float>();
        if (this.left != null)
            values.addAll(this.left.getChildValues());
        if (this.center != null)
            values.addAll(this.center.getChildValues());
        if (this.right != null)
            values.addAll(this.right.getChildValues());
        if (this.isLeaf()) {
            values.add(this.value1);
        }

        return new ArrayList<Float>(values);
    }

    public TwoThreeTree.Branch getChildBranch(Node child) {
        var parent = child.parent;
        if (parent.left.equals(child))
            return TwoThreeTree.Branch.left;
        if (parent.center.equals(child))
            return TwoThreeTree.Branch.center;
        if (parent.right.equals(child))
            return TwoThreeTree.Branch.right;
        return null;
    }

    public String printNode() {
        // if (this.tempValue == 0 && this.tempNode == null) {
        return "N:(" + Double.toString(this.value1) + "," + Double.toString(this.value2) + ")";
        // }
        // else {
        // return "temps not empty!!!";
        // }
    }
}
