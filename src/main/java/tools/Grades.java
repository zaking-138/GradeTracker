package tools;

public class Grades {

    private Grades(){}

    public static boolean isValidScore(double score){
        return score >= 0;
    }

    public static boolean isValidScore(double score, double maxPoints){
        return score >= 0 && score <= maxPoints;
    }

    public static double calculatePercent(double score, double maxPoints){
        if (maxPoints <= 0) return 0;
        return score / maxPoints * 100;
    }

    public static String toLetterGrade(double score, double maxPoints){
        double percent = calculatePercent(score, maxPoints);

        if(percent >= 90) return "A";
        else if(percent >= 80) return "B";
        else if(percent >= 70) return "C";
        else if(percent >= 60) return "D";
        return "F";

    }

    public static String toStringGrade(String title, double score, double maxPoints, String feedback){
        String feed = (feedback == null || feedback.isBlank()) ? "" : feedback;
        double percent = calculatePercent(score, maxPoints);
        String letter =  toLetterGrade(score, maxPoints);

        return title
                + " | Score: " + score + "/" + maxPoints
                + " | Percent: " + String.format("%.2f", percent)
                + " | Letter: " + letter
                + " | Feedback: " + feed;
    }

}
