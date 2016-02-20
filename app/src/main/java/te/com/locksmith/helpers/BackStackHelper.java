package te.com.locksmith.helpers;

import android.app.Fragment;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by enes on 20/02/16.
 */
public class BackStackHelper {
    private static LinkedHashMap<String, Fragment> fragmentLinkedHashMap = new LinkedHashMap<>();

    public static void push(String key, Fragment fragment) {

        if (containsKey(key)) {
            fragmentLinkedHashMap.remove(key);
        }
        fragmentLinkedHashMap.put(key, fragment);

    }

    public static Fragment pop() {
        int lastIndex = fragmentLinkedHashMap.size() - 1;
        Map.Entry entry = getEntry(lastIndex);
        fragmentLinkedHashMap.remove(entry.getKey());
        return (Fragment) entry.getValue();
    }

    public static Fragment getAndRemove(String key){
        Fragment fragment = fragmentLinkedHashMap.get(key);
        fragmentLinkedHashMap.remove(key);
        return fragment;
    }

    public static boolean isEmty() {
        return fragmentLinkedHashMap.isEmpty();
    }

    public static void clearStack(){
        fragmentLinkedHashMap = new LinkedHashMap<>();
    }

    public static boolean containsKey(Object key) {
        return fragmentLinkedHashMap.get(key) != null;
    }

    private static Map.Entry getEntry(int id) {
        Iterator iterator = fragmentLinkedHashMap.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (n == id) {
                return entry;
            }
            n++;
        }
        return null;
    }
}
