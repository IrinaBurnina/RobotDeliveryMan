import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();
    public static int maxNumberOfRepeats;
    public static int keyOfMaxRepeats;

    public static void main(String[] args) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        sb.append("Самое частое количество повторений ");
        new Thread(() -> {
            synchronized (sizeToFreq) {
                String[] routes = new String[1000];
                for (int i = 0; i < routes.length; i++) {
                    routes[i] = generateRoute("RLRFR", 100);
                    System.out.println(routes[i]);
                }
                for (String route : routes) {
                    int countR = 0;
                    for (int i = 0; i < route.length(); i++) {
                        if (route.charAt(i) == 'R') {
                            countR++;
                        }
                    }
                    if (!sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, 1);
                    } else {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    }
                }
                for (Integer key : sizeToFreq.keySet()) {
                    if (sizeToFreq.get(key) > maxNumberOfRepeats) {
                        maxNumberOfRepeats = sizeToFreq.get(key);
                        keyOfMaxRepeats = key;
                    }
                }
                sizeToFreq.notify();
            }
        }).start();
        synchronized (sizeToFreq) {
            sizeToFreq.wait();
            sb.append(" " + keyOfMaxRepeats + " (встретилось " + maxNumberOfRepeats + " раз)" + '\n');
            sizeToFreq.remove(keyOfMaxRepeats);
            sb.append("Другие размеры:");
            for (Integer key : sizeToFreq.keySet()) {
                sb.append(" -" + key + "  (" + sizeToFreq.get(key) + " раз)" + '\n');
            }
        }
        System.out.println(sb);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
