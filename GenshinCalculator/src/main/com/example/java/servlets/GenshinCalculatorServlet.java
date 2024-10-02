import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet("/calculate")
public class GenshinCalculatorServlet extends HttpServlet {

    // Define your constants and methods from the original GenshinCalculator class here
    // (include constants and methods)

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read parameters from the form
        double attack = Double.parseDouble(request.getParameter("attack"));
        double defense = Double.parseDouble(request.getParameter("defense"));
        double hp = Double.parseDouble(request.getParameter("hp"));
        double elementalMastery = Double.parseDouble(request.getParameter("elementalMastery"));
        double talentPercentage = Double.parseDouble(request.getParameter("talentPercentage"));
        String scalingType = request.getParameter("scalingType");
        double critRate = Double.parseDouble(request.getParameter("critRate")) / 100;
        double critDamage = Double.parseDouble(request.getParameter("critDamage")) / 100;
        int level = Integer.parseInt(request.getParameter("level"));
        boolean viridescent = request.getParameter("viridescent") != null;
        boolean deepwood = request.getParameter("deepwood") != null;
        boolean zhongli = request.getParameter("zhongli") != null;

        // Read reaction type
        String reactionChoice = request.getParameter("reaction");
        Reaction reaction = Reaction.valueOf(reactionChoice); // Assuming all valid inputs

        // Perform damage calculations here (similar logic as in main method)

        // Assuming you calculate totalDamage as per your original logic
        double totalDamage = calculateDamage(attack, defense, hp, elementalMastery, talentPercentage, scalingType,
                                              critRate, critDamage, level, viridescent, deepwood, zhongli, reaction);

        // Send the response back to the client
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><body>");
        writer.println("<h2>Total damage after 1000 rotations: " + totalDamage + "</h2>");
        writer.println("<a href='/'>Go back</a>");
        writer.println("</body></html>");
    }

    private double calculateDamage(double attack, double defense, double hp, double elementalMastery,
                                   double talentPercentage, String scalingType, double critRate,
                                   double critDamage, int level, boolean viridescent, boolean deepwood,
                                   boolean zhongli, Reaction reaction) {
        // Your damage calculation logic goes here
        // Return the total damage calculated
        return 0.0; // Placeholder
    }
}
