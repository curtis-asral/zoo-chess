package edu.njit;

import java.util.List;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import static javax.swing.JOptionPane.showMessageDialog;



public class Chess 
{
    public static void main(String[] args) throws ConcurrentModificationException, IOException, InterruptedException {

        Map<Character, Integer> map = new HashMap<>();
        map.put('a', 0);
        map.put('b', 1);
        map.put('c', 2);
        map.put('d', 3);
        map.put('e', 4);
        map.put('f', 5);
        map.put('g', 6);
        map.put('h', 7);
        map.put('1', 7);
        map.put('2', 6);
        map.put('3', 5);
        map.put('4', 4);
        map.put('5', 3);
        map.put('6', 2);
        map.put('7', 1);
        map.put('8', 0);

        Map<Integer, Character> reverseColMap =  new HashMap<>();
        reverseColMap.put(0, 'a');
        reverseColMap.put(1, 'b');
        reverseColMap.put(2, 'c');
        reverseColMap.put(3, 'd');
        reverseColMap.put(4, 'e');
        reverseColMap.put(5, 'f');
        reverseColMap.put(6, 'g');
        reverseColMap.put(7, 'h');

        Map<Integer, Character> reverseRowMap =  new HashMap<>();
        reverseRowMap.put(7, '1');
        reverseRowMap.put(6, '2');
        reverseRowMap.put(5, '3');
        reverseRowMap.put(4, '4');
        reverseRowMap.put(3, '5');
        reverseRowMap.put(2, '6');
        reverseRowMap.put(1, '7');
        reverseRowMap.put(0, '8');

        GUI.mainGUI();

        Board board = new Board();

        while (!board.isDraw() && !board.isMated() && !board.isInsufficientMaterial() && !board.isStaleMate()) {

            if (GUI.moveComplete) {

                GUIPiece p0 = GUIPiece.getGUIPiece(Character.getNumericValue(GUI.s.charAt(2)),Character.getNumericValue(GUI.s.charAt(3)));

                char fr = reverseRowMap.get(Character.getNumericValue(GUI.s.charAt(1)));
                char fc = reverseColMap.get(Character.getNumericValue(GUI.s.charAt(0)));
                char tr = reverseRowMap.get(Character.getNumericValue(GUI.s.charAt(3)));
                char tc = reverseColMap.get(Character.getNumericValue(GUI.s.charAt(2)));

                String m = ""+fc+fr+tc+tr;

                board.doMove(m);

                if (p0.name.equals("king")) {

                    if (m.equals("e1c1"))
                        GUIPiece.getGUIPiece(0,7).move(3,7);
                    if (m.equals("e1g1"))
                        GUIPiece.getGUIPiece(7,7).move(5,7);
                        
                }

                if (board.isDraw() || board.isMated() || board.isInsufficientMaterial() || board.isStaleMate()) {

                    if (board.isDraw()) {
                        showMessageDialog(null, "Draw!");
                    }
                    else if (board.isMated()) {
                        showMessageDialog(null, "Checkmate!");
                    }
                    else if (board.isInsufficientMaterial()) {
                        showMessageDialog(null, "Insufficient Material!");
                    }
                    else if (board.isStaleMate()) {
                        showMessageDialog(null, "Stalemate!");
                    }
                    
                    TimeUnit.MINUTES.sleep(1);
                    System.exit(1);

                }

                String bestMove = minimaxRoot(4, board, true);

                board.doMove(bestMove);

                int fr2 = map.get(bestMove.charAt(1));
                int fc2 = map.get(bestMove.charAt(0));
                int tr2 = map.get(bestMove.charAt(3));
                int tc2 = map.get(bestMove.charAt(2));

                GUIPiece p = GUIPiece.getGUIPiece(fc2, fr2);

                p.move(tc2, tr2);

                if (p.name.equals("king") && Math.abs(tc2-fc2) > 1) {
                    if (tc2 == 2)
                        GUIPiece.getGUIPiece(0,0).move(3,0);
                    if (tc2 == 6)
                        GUIPiece.getGUIPiece(7,0).move(5,0);
                }

                GUI.moveComplete = false;
            
            }

        }

        if (board.isDraw())
            showMessageDialog(null, "Draw!");

        else if (board.isMated())
            showMessageDialog(null, "CheckMate!");

        else if (board.isInsufficientMaterial())
            showMessageDialog(null, "Insufficient Material!");

        else if (board.isStaleMate())
            showMessageDialog(null, "Stalemate!");

        TimeUnit.MINUTES.sleep(1);
        System.exit(1);

    }

    static double evaluate(Board board) {

        double sum = 0;

        double[][] pawnEvalWhite =
        {
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
            {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
            {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
            {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
            {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
            {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
        };

        double[][] pawnEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                pawnEvalBlack[8-1-i][8-1-j] = pawnEvalWhite[i][j];
            }
        }

        double[][] knightEvalWhite =
        {
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
            {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
            {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
            {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
            {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
            {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
            {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
        };

        double[][] knightEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                knightEvalBlack[8-1-i][8-1-j] = knightEvalWhite[i][j];
            }
        }

        double[][] bishopEvalWhite = 
        {
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
            { -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
            { -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
            { -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
            { -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
        };

        double[][] bishopEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                bishopEvalBlack[8-1-i][8-1-j] = bishopEvalWhite[i][j];
            }
        }

        double[][] rookEvalWhite = 
        {
            {  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            {  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
            { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {  0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
        };

        double[][] rookEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                rookEvalBlack[8-1-i][8-1-j] = rookEvalWhite[i][j];
            }
        }

        double[][] queenEvalWhite =
        {
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            { -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            {  0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            { -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
        };

        double[][] queenEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                queenEvalBlack[8-1-i][8-1-j] = queenEvalWhite[i][j];
            }
        }

        double[][] kingEvalWhite = 
        {
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0 },
            {  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0 }
        };

        double[][] kingEvalBlack = new double[8][8];
        for(int i = 8-1; i >= 0; i--) {
            for(int j = 8-1; j >= 0; j--) {
                kingEvalBlack[8-1-i][8-1-j] = kingEvalWhite[i][j];
            }
        }

        Piece[] currentBoard = board.boardToArray();
        for (int i = 0; i < 64; i++ ) {

            int num = 63-i;
            int row = num/8;
            int col = num%8;

            if (currentBoard[i] == Piece.BLACK_BISHOP) {
                sum -= 30;
                sum += bishopEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.BLACK_KING) {
                sum -= 900;
                sum += kingEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.BLACK_KNIGHT) {
                sum -= 30;
                sum += knightEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.BLACK_PAWN) {
                sum -= 10;
                sum += pawnEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.BLACK_QUEEN) {
                sum -= 90;
                sum += queenEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.BLACK_ROOK) {
                sum -= 50;
                sum += rookEvalBlack[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_BISHOP) {
                sum += 30;
                sum += bishopEvalWhite[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_KING) {
                sum += 900;
                sum += kingEvalWhite[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_KNIGHT) {
                sum += 30;
                sum += knightEvalWhite[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_PAWN) {
                sum += 10;
                sum += pawnEvalWhite[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_QUEEN) {
                sum += 90;
                sum += queenEvalWhite[row][col];
            }
            else if (currentBoard[i] == Piece.WHITE_ROOK) {
                sum += 50;
                sum += rookEvalWhite[row][col];
            }
            
        }

        return sum;

    }

    static String minimaxRoot(int depth, Board board, boolean isMaximisingPlayer) {

        List<Move> moves = board.legalMoves();
        double bestMove = 9999;
        String bestMoveFound = "";
    
        for(int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            board.doMove(move);
            double value = minimax(depth - 1, board, isMaximisingPlayer,0);
            board.undoMove();
            if(value <= bestMove) {
                bestMove = value;
                bestMoveFound = move.toString();
            }
        }
        return bestMoveFound;
    }
    
    static double minimax(int depth, Board board, boolean isMaximisingPlayer, int positionCount) {
        positionCount++;
        if (depth == 0)
            return evaluate(board);
    
        List<Move> moves = board.legalMoves();
    
        if (isMaximisingPlayer) {
            double bestMove = -9999;
            for (int i = 0; i < moves.size(); i++) {
                board.doMove(moves.get(i));
                bestMove = Math.max(bestMove, minimax(depth - 1, board, !isMaximisingPlayer, positionCount));
                board.undoMove();
            }
            return bestMove;
        } else {
            double bestMove = 9999;
            for (int i = 0; i < moves.size(); i++) {
                board.doMove(moves.get(i));
                bestMove = Math.min(bestMove, minimax(depth - 1, board, !isMaximisingPlayer,positionCount));
                board.undoMove();
            }
            return bestMove;
        }
    }

}
