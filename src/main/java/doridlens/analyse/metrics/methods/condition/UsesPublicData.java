package doridlens.analyse.metrics.methods.condition;

import soot.SootMethod;

import java.util.regex.Pattern;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:13
 * Description:
 */
public class UsesPublicData extends MethodCondition {

    public static final String NAME = "uses_public_data";

    private static final String REGEX =
            "openFileOutput\\(java\\.lang\\.String,int\\)>\\(\"[^\"]*\", [12]\\)";

    private Pattern pattern;

    public UsesPublicData() {
        super(NAME);
        this.pattern = Pattern.compile(REGEX);
    }

    @Override
    public boolean matches(SootMethod item) {
        String body = item.getActiveBody().toString();
        if (body.contains("openFileOutput")) {
            return pattern.matcher(body).find();
        }
        return false;
    }

}

