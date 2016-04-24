package ohtu;

public class TennisGame {

    private int player1Score = 0;
    private int player2Score = 0;
    private String player1Name;
    private String player2Name;

    public TennisGame(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public void wonPoint(String playerName) {
        if (playerName.equals(player1Name)) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    public String getScore() {
        StringBuilder sb = new StringBuilder();

        if (player1Score == player2Score) {
            appendScoreWhenEven(sb);
        } else if (player1Score >= 4 || player2Score >= 4) {
            appendScoreWhenAdvantageOrWin(sb);
        } else {
            appendScoreInOtherSituations(sb);
        }
        return sb.toString();
    }

    private void appendScoreWhenEven(StringBuilder sb) {
        if (player1Score == 4) {
            sb.append("Deuce");
        } else {
            sb.append(scoreToString(player1Score));
            sb.append("-All");
        }
    }

    private void appendScoreWhenAdvantageOrWin(StringBuilder sb) {
        int score1MinusScore2 = player1Score - player2Score;
        if (score1MinusScore2 == 1) {
            sb.append("Advantage player1");
        } else if (score1MinusScore2 == -1) {
            sb.append("Advantage player2");
        } else if (score1MinusScore2 >= 2) {
            sb.append("Win for player1");
        } else {
            sb.append("Win for player2");
        }
    }
    
    private void appendScoreInOtherSituations(StringBuilder sb) {
        sb.append(scoreToString(player1Score));
        sb.append("-");
        sb.append(scoreToString(player2Score));
    }

    private String scoreToString(int score) {
        switch (score) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            case 3:
                return "Forty";
        }
        return "";
    }
}
