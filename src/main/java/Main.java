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
                //генерация маршрута
                for (int i = 0; i < routes.length; i++) {
                    routes[i] = generateRoute("RLRFR", 100);
                    System.out.println(routes[i]);
                }
                for (String route : routes) {

                    int partSize = 0;
                    for (int i = 0; i < route.length(); i++) {
                        if (route.charAt(i) == 'R') {
                            partSize++;
                            continue;
                        }
                        if (sizeToFreq.containsKey(partSize) && sizeToFreq.size() != 0 && partSize != 0) {
                            int number = sizeToFreq.get(partSize) + 1;
                            sizeToFreq.put(partSize, number);
                            partSize = 0;
                        } else if (!sizeToFreq.containsKey(partSize) && partSize != 0) {
                            sizeToFreq.put(partSize, 1);
                            partSize = 0;
                        }
                        for (Integer key : sizeToFreq.keySet()) {
                            if (sizeToFreq.get(key) > maxNumberOfRepeats) {
                                maxNumberOfRepeats = sizeToFreq.get(key);
                                keyOfMaxRepeats = key;
                            }
                        }
                    }
                }
                sizeToFreq.notify();
            }
        }).start();
        synchronized (sizeToFreq) {
            sizeToFreq.wait();
            sb.append(" " + keyOfMaxRepeats + " (встретилось " + maxNumberOfRepeats + " раз)" + '\n');
            sizeToFreq.remove(keyOfMaxRepeats);
            for (Integer key : sizeToFreq.keySet()) {
                sb.append("Другие размеры: -" + key + "  (" + sizeToFreq.get(key) + " раз)" + '\n');
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
