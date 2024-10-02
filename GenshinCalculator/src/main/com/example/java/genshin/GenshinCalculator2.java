import java.util.Scanner;

public class GenshinCalculator2 {

    // Constants for damage calculation
    private static final double BASE_DAMAGE_MULTIPLIER = 1.0;
    private static final double ADDITIVE_BASE_DAMAGE_BONUS = 0;
    private static final double DMG_BONUS = 1.0;
    private static final double DMG_REDUCTION_TARGET = 0; // Assuming no damage reduction
    private static final double DEF_MULTIPLIER_TARGET = 0.5; // Assuming target has 50% DEF reduction
    private static final double RES_MULTIPLIER_TARGET = 0.9; // Default resistance multiplier (before applying debuffs)
    private static final double AMPLIFYING_MULTIPLIER_VAPORIZE_PYRO = 1.5;
    private static final double AMPLIFYING_MULTIPLIER_VAPORIZE_HYDRO = 2.0;
    private static final double AMPLIFYING_MULTIPLIER_MELT_CRYO = 1.5;
    private static final double AMPLIFYING_MULTIPLIER_MELT_PYRO = 2.0;

    // Constants for transformative reactions
    private static final double REACTION_MULTIPLIER_BLOOM = 3.0;
    private static final double REACTION_MULTIPLIER_BURGEON = 3.0;
    private static final double REACTION_MULTIPLIER_HYPERBLOOM = 3.0;

    // Constants for additive reactions
    private static final double REACTION_MULTIPLIER_SPREAD = 1.25;
    private static final double REACTION_MULTIPLIER_AGGRAVATE = 1.15;

    // Resistance debuff constants
    private static final double VIRIDESCENT_REDUCTION = 0.4; // 40% reduction for infused element
    private static final double DEEPWOOD_REDUCTION = 0.3; // 30% reduction for Dendro
    private static final double ZHONGLI_SHIELD_REDUCTION = 0.2; // 20% reduction for all elements

    // Reaction types
    private enum Reaction {
        VAPORIZE_PYRO,
        VAPORIZE_HYDRO,
        MELT_CRYO,
        MELT_PYRO,
        BLOOM,
        BURGEON,
        HYPERBLOOM,
        AGGRAVATE,
        SPREAD
    }

    // Damage multiplier by level for transformative and additive reactions
    private static double getLevelMultiplier(int level) {
        return switch (level) {
            case 70 -> 765.64;
            case 71 -> 794.77;
            case 72 -> 824.68;
            case 73 -> 851.16;
            case 74 -> 877.74;
            case 75 -> 914.23;
            case 76 -> 946.75;
            case 77 -> 979.41;
            case 78 -> 1011.22;
            case 79 -> 1044.79;
            case 80 -> 1077.44;
            case 81 -> 1110.00;
            case 82 -> 1142.98;
            case 83 -> 1176.37;
            case 84 -> 1210.18;
            case 85 -> 1253.84;
            case 86 -> 1288.95;
            case 87 -> 1325.48;
            case 88 -> 1363.46;
            case 89 -> 1405.10;
            case 90 -> 1446.85;
            default -> 1.0; // default for invalid levels
        };
    }

    // Method to apply resistance debuffs from artifacts and skills
    private static double applyResistanceDebuffs(double baseResMultiplier, boolean viridescent, boolean deepwood, boolean zhongli, String element) {
        // Apply Viridescent reduction for the infused element
        if (viridescent) {
            baseResMultiplier *= (1 - VIRIDESCENT_REDUCTION);
        }
        // Apply Deepwood reduction for Dendro reactions
        if (deepwood) {
            if (element.equals("Dendro") || element.equals("Hyperbloom") || element.equals("Spread") || element.equals("Aggravate") || element.equals("Burgeon")) {
                baseResMultiplier *= (1 - DEEPWOOD_REDUCTION);
            }
        }
        // Apply Zhongli shield reduction for all elements
        if (zhongli) {
            baseResMultiplier *= (1 - ZHONGLI_SHIELD_REDUCTION);
        }
        return baseResMultiplier;
    }

    // Method to calculate transformative reaction damage
    private static double calculateTransformativeReaction(double em, double reactionMultiplier, double reactionBonus, int level, double resMultiplier) {
        double levelMultiplier = getLevelMultiplier(level);
        return reactionMultiplier * levelMultiplier * (1 + (16 * em) / (2000 + em) + reactionBonus) * resMultiplier;
    }

    // Method to calculate additive reaction damage
    private static double calculateAdditiveReaction(double em, double reactionMultiplier, double reactionBonus, int level, double resMultiplier) {
        double levelMultiplier = getLevelMultiplier(level);
        return reactionMultiplier * levelMultiplier * (1 + (5 * em) / (1200 + em) + reactionBonus) * resMultiplier;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input character stats
        System.out.println("Input your character's stats:");

        System.out.print("Attack: ");
        double attack = scanner.nextDouble();

        System.out.print("Defense: ");
        double defense = scanner.nextDouble();

        System.out.print("HP: ");
        double hp = scanner.nextDouble();

        System.out.print("Elemental Mastery: ");
        double elementalMastery = scanner.nextDouble();

        System.out.print("Talent Scaling Percentage (as a decimal, e.g., 1.5 for 150%): ");
        double talentPercentage = scanner.nextDouble();

        System.out.print("Is the talent scaling with ATK, DEF, HP, or EM?: ");
        String scalingType = scanner.next();

        System.out.print("Crit Rate (in percentage, e.g., 50 for 50%): ");
        double critRate = scanner.nextDouble() / 100;

        System.out.print("Crit Damage (in percentage, e.g., 100 for 100%): ");
        double critDamage = scanner.nextDouble() / 100;

        System.out.print("Character level (e.g., 70 to 90): ");
        int level = scanner.nextInt();

        // Check if debuffs from artifacts or skills apply
        System.out.print("Is the Viridescent Venerer artifact equipped (true/false)? ");
        boolean viridescent = scanner.nextBoolean();

        System.out.print("Is the Deepwood Memories artifact equipped (true/false)? ");
        boolean deepwood = scanner.nextBoolean();

        System.out.print("Is Zhongli's shield active (true/false)? ");
        boolean zhongli = scanner.nextBoolean();

        // Select elemental reaction
        System.out.println("Choose an elemental reaction:");
        System.out.println("1. Vaporize (Pyro x1.5)");
        System.out.println("2. Vaporize (Hydro x2)");
        System.out.println("3. Melt (Cryo x1.5)");
        System.out.println("4. Melt (Pyro x2)");
        System.out.println("5. Bloom");
        System.out.println("6. Burgeon");
        System.out.println("7. Hyperbloom");
        System.out.println("8. Aggravate");
        System.out.println("9. Spread");

        int reactionChoice = scanner.nextInt();
        Reaction reaction;

        switch (reactionChoice) {
            case 1:
                reaction = Reaction.VAPORIZE_PYRO;
                break;
            case 2:
                reaction = Reaction.VAPORIZE_HYDRO;
                break;
            case 3:
                reaction = Reaction.MELT_CRYO;
                break;
            case 4:
                reaction = Reaction.MELT_PYRO;
                break;
            case 5:
                reaction = Reaction.BLOOM;
                break;
            case 6:
                reaction = Reaction.BURGEON;
                break;
            case 7:
                reaction = Reaction.HYPERBLOOM;
                break;
            case 8:
                reaction = Reaction.AGGRAVATE;
                break;
            case 9:
                reaction = Reaction.SPREAD;
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        // Reaction Bonus for transformative reactions
        double reactionBonus = 0.0; // Assuming no set bonuses

        // Calculate resistance multiplier after applying debuffs
        String element = "";
        double attackScaling = 0; // Attack scaling

        if (scalingType.equalsIgnoreCase("ATK")) {
            attackScaling = attack * talentPercentage;
        } else if (scalingType.equalsIgnoreCase("DEF")) {
            attackScaling = defense * talentPercentage;
        } else if (scalingType.equalsIgnoreCase("HP")) {
            attackScaling = hp * talentPercentage;
        } else if (scalingType.equalsIgnoreCase("EM")) {
            attackScaling = elementalMastery * talentPercentage;
        }

        if (reaction == Reaction.VAPORIZE_PYRO || reaction == Reaction.MELT_PYRO) {
            element = "Pyro";
        } else if (reaction == Reaction.VAPORIZE_HYDRO) {
            element = "Hydro";
        } else if (reaction == Reaction.BLOOM || reaction == Reaction.BURGEON || reaction == Reaction.HYPERBLOOM) {
            element = "Dendro";
        } else {
            element = "Other"; // For Aggravate and Spread, which also use Dendro for Deepwood reduction
        }

        double resMultiplier = applyResistanceDebuffs(RES_MULTIPLIER_TARGET, viridescent, deepwood, zhongli, element);

        // Number of rotations
        int rotations = 1000;
        double totalDamage = 0.0;

        // Loop for 1000 rotations to calculate total damage
        for (int i = 0; i < rotations; i++) {
            // Handle transformative reactions
            if (reaction == Reaction.BLOOM || reaction == Reaction.BURGEON || reaction == Reaction.HYPERBLOOM) {
                double reactionMultiplier = switch (reaction) {
                    case BLOOM -> REACTION_MULTIPLIER_BLOOM;
                    case BURGEON -> REACTION_MULTIPLIER_BURGEON;
                    case HYPERBLOOM -> REACTION_MULTIPLIER_HYPERBLOOM;
                    default -> 1.0;
                };
                totalDamage += calculateTransformativeReaction(elementalMastery, reactionMultiplier, reactionBonus, level, resMultiplier);
            }

            // Handle additive reactions
            if (reaction == Reaction.AGGRAVATE || reaction == Reaction.SPREAD) {
                double reactionMultiplier = switch (reaction) {
                    case AGGRAVATE -> REACTION_MULTIPLIER_AGGRAVATE;
                    case SPREAD -> REACTION_MULTIPLIER_SPREAD;
                    default -> 1.0;
                };
                totalDamage += calculateAdditiveReaction(elementalMastery, reactionMultiplier, reactionBonus, level, resMultiplier);
            }

            // Handle Vaporize damage
            if (reaction == Reaction.VAPORIZE_PYRO || reaction == Reaction.VAPORIZE_HYDRO) {
                double reactionMultiplier = (reaction == Reaction.VAPORIZE_PYRO) ? AMPLIFYING_MULTIPLIER_VAPORIZE_PYRO : AMPLIFYING_MULTIPLIER_VAPORIZE_HYDRO;
                totalDamage += calculateTransformativeReaction(elementalMastery, reactionMultiplier, reactionBonus, level, resMultiplier);
            }
        }

        // Output the total damage after 1000 rotations
        System.out.println("Total damage after 1000 rotations: " + totalDamage);
    }
}
