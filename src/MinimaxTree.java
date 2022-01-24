import java.security.spec.RSAOtherPrimeInfo;
import java.util.HashSet;
import java.util.Set;

public class MinimaxTree {
    Node root;
    int player;
    int depthLimit;
    int evaluationValue;
    int totalStates = 0;
    int evaluationFunctionNum;

    MinimaxTree(int[][] boardData, int player, int depthLimit, int evaluationFunctionNum){
        root = new Node(null, boardData, player, 1);
        this.player = player;
        this.depthLimit = depthLimit;
        this.evaluationFunctionNum = evaluationFunctionNum;
    }

    public void buildTree(Node root){
        int stop = 0;
        //Enable depth limit
        if(depthLimit != -1){
            //stop build tree
            if(root.depth == depthLimit){
                stop = 1;
            }
        }
        if(stop != 1){
            //System.out.println("--------------depth: " + root.depth);
            root.addChildren();
            if(root.children.length != 0){
                for(Node child : root.children){
                    buildTree(child);
                }
            }
        }
    }

    public void calTotalStates(Node root){
        if(root.children != null && root.children.length != 0){
            //System.out.println("root.children.length " + root.children.length);
            for(Node child : root.children){
                calTotalStates(child);
            }
        } else {
            //System.out.println("differ: " + root.evaluationDiffer);
            //root.board.print();
        }
        totalStates++;
    }
    public void setEvaluationValue(Node root){
        for(Node child : root.children){
            if(child.depth < depthLimit) {
                setEvaluationValue(child);
            } else {
                if(evaluationFunctionNum == 0){
                    evaluationFunctionOne(child);
                } else {
                    evaluationFunctionTwo(child);
                }
                //child.board.print();
            }
        }
        int MAXorMin = 0;//1 MAX 0 MIN
        if(root.depth % 2 == 1){
            //MAX
            //System.out.println("MAX");
            MAXorMin = 1;

        } else {
            //MIN
            //System.out.println("MIN");
            MAXorMin = 0;

        }
        int smallestIndex = -1;
        float smallestValue = 45;
        int biggerIndex = -1;
        float biggerValue = -45;
        for(int i = 0; i < root.children.length; i++){
            Node child = root.children[i];
            if(MAXorMin == 0){
                if(child.evaluationValue <= smallestValue){
                    child.evaluationValue = smallestValue;
                    smallestIndex = i;
                }
            } else if(MAXorMin == 1){
                if(child.evaluationValue >= biggerValue){
                    child.evaluationValue = biggerValue;
                    biggerIndex = i;
                }
            }
        }
        if(MAXorMin == 0){
            root.evaluationValue = smallestValue;
            root.pathIndex = smallestIndex;
        } else if(MAXorMin == 1){
            root.evaluationValue = biggerValue;
            root.pathIndex = biggerIndex;
        }
    }

    public int[][] getNextBoardData(Node root){
        return root.children[root.pathIndex].board.boardData;
    }

    public void setEvaluationValueAlphaBeta(Node root){
        int isLeafNode = 0;
        int MAXorMin = 0;//1 MAX 0 MIN
        if(root.depth % 2 == 1){
            //MAX
            MAXorMin = 1;
        } else {
            //MIN
            MAXorMin = 0;
        }
        if(root.children != null && root.children.length != 0){
            //not leaf node
            isLeafNode = 0;
            for(Node child : root.children){
                if(root.alterBeta >= root.alterAlpha){
                    child.alterAlpha = root.alterAlpha;
                    child.alterBeta = root.alterBeta;
                    setEvaluationValueAlphaBeta(child);
                } else {
                    break;
                }
            }
            if(MAXorMin == 0){
                root.evaluationValue = root.alterBeta;
            } else if(MAXorMin == 1){
                root.evaluationValue = root.alterAlpha;
            }
        } else {
            //is leaf node
            isLeafNode = 1;
            if(evaluationFunctionNum == 0){
                evaluationFunctionOne(root);
            } else {
                evaluationFunctionTwo(root);
            }
        }
        totalStates++;
        //System.out.println("root.depth: " + root.depth);
        //System.out.println("root.alterAlpha: " + root.alterAlpha + " root.alterBeta: " + root.alterBeta);
        //System.out.println("root.evaluationValue: " + root.evaluationValue);
        if(root.parent != null){
            if(MAXorMin == 1){
                if(root.evaluationValue < root.parent.alterBeta){
                    root.parent.alterBeta = root.evaluationValue;
                }
            } else if(MAXorMin == 0){
                if(root.evaluationValue > root.parent.alterAlpha){
                    root.parent.alterAlpha = root.evaluationValue;
                }
            }
        }

    }

    public int[][] getPathAlphaBeta(Node root){
        for(Node child : root.children){
            if(child.evaluationValue == root.evaluationValue){
                return child.board.boardData;
            }
        }
        return null;
    }

    private void evaluationFunctionOne(Node root){
        int[] nums = root.countPieces();
        root.evaluationValue = nums[root.player] - nums[root.opponent];
    }

    private void evaluationFunctionTwo(Node root){
        int[] nums = root.countPieces();
        //root.evaluationValue = (float)nums[root.player] / (nums[0] + nums[1]);
        root.evaluationValue = nums[root.player];
    }

    private boolean closer(float a, float b, float evaluationValue){
        float distanceA = a - evaluationValue;
        float distanceB = b - evaluationValue;
        if(distanceA < 0){
            distanceA = -distanceA;
        }
        if(distanceB < 0){
            distanceB = -distanceB;
        }
        if(distanceA < distanceB){
            return true;
        } else {
            return false;
        }
    }

    private boolean farther(float a, float b, float evaluationValue){
        float distanceA = a - evaluationValue;
        float distanceB = b - evaluationValue;
        if(distanceA < 0){
            distanceA = -distanceA;
        }
        if(distanceB < 0){
            distanceB = -distanceB;
        }
        if(distanceA > distanceB){
            return true;
        } else {
            return false;
        }
    }

    public class Node{
        Node parent;
        Node[] children;
        Board board;
        int player;
        int opponent;
        float evaluationValue;
        int pathIndex;
        int depth;
        float alterAlpha;
        float alterBeta;

        Node(Node parent, int[][] boardData, int player, int depth){
            this.parent = parent;
            this.board = new Board(boardData);
            this.player = player;
            this.depth = depth;
            alterAlpha = -(board.row * board.column);
            alterBeta = board.row * board.column;
            evaluationValue = board.row * board.column;
            if(player == 0){
                opponent = 1;
            } else if(player == 1){
                opponent = 0;
            }

        }

        public void addChildren(){
            Set<Piece> actions = findAllActions();
            int[][] childBoardData = new int[0][0];
            //count action number
            int count = 0;
            for(Piece p : actions){
                count += p.captureNum.size();
                count += p.retreatNum.size();
            }
            if(count == 0){
                for(Piece p : actions){
                    count += p.empties.size();
                }
            }
            children = new Node[count];
            int childCount = 0;
            for(Piece p : actions){
                //System.out.println("***********p data: " + p.x + " " + p.y + " " + p.color);
                //capture && retreat
                if(p.captureNum.size() != 0 || p.retreatNum.size() != 0){
                    if(p.captureNum.size() != 0){
                        //System.out.println("capture:");
                        for(Set<int[]> si : p.captureNum){
                            childBoardData = newBoardData(si, p);
                            Node child = new Node(this, childBoardData, opponent, depth + 1);
                            children[childCount] = child;
                            childCount++;
                        }
                    }
                    if(p.retreatNum.size() != 0){
                        //System.out.println("retreat:");
                        for(Set<int[]> si : p.retreatNum) {
                            childBoardData = newBoardData(si, p);
                            Node child = new Node(this, childBoardData, opponent, depth + 1);
                            children[childCount] = child;
                            childCount++;
                        }
                    }
                } else {
                    //System.out.println("empty: ");
                    //empty
                    for(int[] iA : p.empties) {
                        childBoardData = new int[p.boardData.length][p.boardData[0].length];
                        for(int i = 0; i < p.boardData.length; i++){
                            for(int j = 0; j < p.boardData[0].length; j++){
                                childBoardData[i][j] = p.boardData[i][j];
                            }
                        }
                        childBoardData[iA[0]][iA[1]] = p.color;
                        childBoardData[p.y][p.x] = -1;
                        Node child = new Node(this, childBoardData, opponent, depth + 1);
                        children[childCount] = child;
                        childCount++;
                    }
                }
            }

        }

        private int[][] newBoardData(Set<int[]> si, Piece p){
            int[] empty = new int[2];
            int[][] newBoardData = new int[p.boardData.length][p.boardData[0].length];
            for(int i = 0; i < p.boardData.length; i++){
                for(int j = 0; j < p.boardData[0].length; j++){
                    newBoardData[i][j] = p.boardData[i][j];
                }
            }
            for(int[] i : si){
                if(p.boardData[i[0]][i[1]] == -1){
                    empty[0] = i[0];
                    empty[1] = i[1];
                } else {
                    newBoardData[i[0]][i[1]] = -1;
                }
            }
            newBoardData[empty[0]][empty[1]] = p.color;
            newBoardData[p.y][p.x] = -1;
            return newBoardData;
        }

        private Set<Piece> findAllActions(){
            Set<Piece> movablePieces = board.calPieceRemove(player);
            Set<Piece> result = new HashSet<>();
            for(Piece p : movablePieces){
                //capture
                if(p.captureNum.size() != 0){
                    result.add(p);
                }
                //retreat
                if(p.retreatNum.size() != 0){
                    result.add(p);
                }
            }
            //empty
            if(result.size() == 0){
                for(Piece p : movablePieces){
                    result.add(p);
                }
            }
            return result;
        }

        public int[] countPieces(){
            int[] result = new int[]{0, 0};
            for(int i = 0; i < board.boardData.length; i++){
                for(int j = 0; j < board.boardData[0].length; j++){
                    if(board.boardData[i][j] == 0){
                        result[0]++;
                    } else if(board.boardData[i][j] == 1){
                        result[1]++;
                    }
                }
            }
            return result;
        }
    }
}
