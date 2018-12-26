package pl.plajerlair.core.rewards;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

/**
 * @author Plajer
 * <p>
 * Created at 26.12.2018
 *
 * @since 1.4.7
 */
public class Reward {

    private RewardExecutor executor;
    private String executableCode;
    private double chance;

    public Reward(String rawCode) {
        String processedCode = rawCode;

        //set reward executor based on provided code
        if (rawCode.contains("p:")) {
            this.executor = RewardExecutor.PLAYER;
            processedCode = StringUtils.replace(processedCode, "p:", "");
        } else if (rawCode.contains("script:")) {
            this.executor = RewardExecutor.SCRIPT;
            processedCode = StringUtils.replace(processedCode, "script:", "");
        } else {
            this.executor = RewardExecutor.CONSOLE;
        }

        //search for chance modifier
        if (processedCode.contains("chance(")) {
            int loc = processedCode.indexOf(")");
            //modifier is invalid
            if (loc == -1) {
                Bukkit.getLogger().warning("rewards.yml configuration is broken! Make sure you don't forget using ')' character in chance condition! Command: " + rawCode);
                //invalid code, 0% chance to execute
                this.chance = 0.0;
                return;
            }
            String chanceStr = processedCode;
            chanceStr = chanceStr.substring(0, loc).replaceAll("[^0-9]+", "");
            double chance = Double.parseDouble(chanceStr);
            processedCode = StringUtils.replace(processedCode, "chance(" + chanceStr + "):", "");
            this.chance = chance;
        } else {
            this.chance = 100.0;
        }
        this.executableCode = processedCode;
    }

    public RewardExecutor getExecutor() {
        return executor;
    }

    public String getExecutableCode() {
        return executableCode;
    }

    public double getChance() {
        return chance;
    }


    public enum RewardExecutor {
        CONSOLE, PLAYER, SCRIPT
    }

}
