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
        final double[] index = {0};
        return IntStream.of(values)
                .distinct()
                .map(i -> -i)
                .sorted()
                .map(i -> (int) (-i * Math.pow(10, index[0]++)))
                .sum();
    }

    List<Integer> oddOrEven(List<Integer> integers) {
        boolean isEven = integers.stream().reduce(0, Integer::sum) % 2 == 0;
        return IntStream.range(0, integers.size())
                .filter(i -> (isEven && i % 2 == 0) || (!isEven && i % 2 != 0))
                .mapToObj(integers::get)
                .collect(Collectors.toList());
    }
}
