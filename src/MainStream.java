import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStream {
    public static void main(String[] args) {
        MainStream ms = new MainStream();
        System.out.println(ms.minValue(new int[]{1,2,3,3,2,3}));
        System.out.println(ms.oddOrEven(Arrays.asList(1,2,3,4,5,6)));
    }

    int minValue(int[] values) {
        return IntStream.of(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, i) -> {
                    int l = (int) Math.floor(Math.log10(i) + 1);
                    return acc * (int) Math.pow(10, l) + i;
                });
    }

    List<Integer> oddOrEven(List<Integer> integers) {
        boolean isEven = integers.stream().reduce(0, Integer::sum) % 2 == 0;
        return integers.stream()
                .filter(i -> isEven == (i % 2 != 0))
                .collect(Collectors.toList());
    }
}
