package doridlens.query.neo4j.queries.antipatterns.ui;

import doridlens.query.neo4j.queries.PaprikaQuery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:31
 * Description:
 */
public class UHA extends PaprikaQuery {

    public static final String KEY = "UHA";

    private static final String ANDROID_CANVAS = "android.graphics.Canvas";
    private static final String ANDROID_PAINT = "android.graphics.Paint";
    private static final int UNSUPPORTED = -1;

    private static final Map<String, Integer> UHA_OPS = new HashMap<>();

    static {
        addUHAMethod("drawVertices", ANDROID_CANVAS, UNSUPPORTED);
        addUHAMethod("setLinearText", ANDROID_PAINT, UNSUPPORTED);
        addUHAMethod("setMaskFilter", ANDROID_PAINT, UNSUPPORTED);
        addUHAMethod("setPathEffect", ANDROID_PAINT, UNSUPPORTED);
        addUHAMethod("setRasterizer", ANDROID_PAINT, UNSUPPORTED);
        addUHAMethod("setSubpixelText", ANDROID_PAINT, UNSUPPORTED);

        addUHAMethod("drawPath", ANDROID_CANVAS, 11);

        addUHAMethod("drawPosText", ANDROID_CANVAS, 16);
        addUHAMethod("drawTextOnPath", ANDROID_CANVAS, 16);
        addUHAMethod("setDrawFilter", ANDROID_CANVAS, 16);
        addUHAMethod("setAntiAlias", ANDROID_PAINT, 16);

        addUHAMethod("setFilterBitmap", ANDROID_PAINT, 17);

        addUHAMethod("drawBitmapMesh", ANDROID_CANVAS, 18);
        addUHAMethod("setStrokeCap", ANDROID_PAINT, 18);

        addUHAMethod("drawPicture", ANDROID_CANVAS, 23);
    }

    private static void addUHAMethod(String method, String aClass, int apiSupported) {
        UHA_OPS.put(method + "#" + aClass, apiSupported);
    }

    public UHA() {
        super(KEY);
    }

    /*
        MATCH (a:App)-[:APP_OWNS_CLASS]->(:Class)-[:CLASS_OWNS_METHOD]->
            (m:Method)-[:CALLS]->(e:ExternalMethod)
        WHERE e.full_name in UHAs
        RETURN m.app_key

        details -> m.full_name as full_name
        else -> count(m) as UHA
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(:Class)-[:CLASS_OWNS_METHOD]->\n" +
                "   (m:Method)-[:CALLS]->(e:ExternalMethod)\n" +
                "WHERE " + isUHAMethod() + "\n" +
                "RETURN m.app_key as app_key,";
        if (details) {
            query += "m.full_name as full_name";
        } else {
            query += "count(m) as UHA";
        }
        return query;
    }

    private String isUHAMethod() {
        Iterator<Map.Entry<String, Integer>> it = UHA_OPS.entrySet().iterator();
        Map.Entry<String, Integer> first = it.next();
        StringBuilder base = new StringBuilder("((" + getMethodApiCondition(first.getKey(), first.getValue()) + ")");
        while (it.hasNext()) {
            Map.Entry<String, Integer> element = it.next();
            base.append("OR(").append(getMethodApiCondition(element.getKey(), element.getValue())).append(")");
        }
        return base.toString() + ")";
    }

    private String getMethodApiCondition(String call, int api) {
        String result = nameMatches(call);
        if (api != UNSUPPORTED) {
            result += " AND (a.target_sdk < " + api + ")";
        }
        return result + "\n";
    }

    private String nameMatches(String call) {
        return "(e.full_name ='" + call + "')";
    }

}