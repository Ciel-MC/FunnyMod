package hk.eric.funnymod.utils.classes;

public class Converters {
    public static TwoWayFunction<Integer, String> INTEGER_CONVERTER = new TwoWayFunction<>() {
        @Override
        public String convert(Integer integer) {
            return integer.toString();
        }

        @Override
        public Integer revert(String s) {
            return Integer.parseInt(s);
        }
    };

    public static TwoWayFunction<Double, String> DOUBLE_CONVERTER = new TwoWayFunction<>() {
        @Override
        public String convert(Double aDouble) {
            return aDouble.toString();
        }

        @Override
        public Double revert(String s) {
            return Double.parseDouble(s);
        }
    };

    public static TwoWayFunction<Long, String> LONG_CONVERTER = new TwoWayFunction<>() {
        @Override
        public String convert(Long aLong) {
            return aLong.toString();
        }

        @Override
        public Long revert(String s) {
            return Long.parseLong(s);
        }
    };

    public static TwoWayFunction<Float, String> FLOAT_CONVERTER = new TwoWayFunction<>() {
        @Override
        public String convert(Float aFloat) {
            return aFloat.toString();
        }

        @Override
        public Float revert(String s) {
            return Float.parseFloat(s);
        }
    };

    public static TwoWayFunction<Boolean, String> BOOLEAN_CONVERTER = new TwoWayFunction<>() {
        @Override
        public String convert(Boolean aBoolean) {
            return aBoolean.toString();
        }

        @Override
        public Boolean revert(String s) {
            return Boolean.parseBoolean(s);
        }
    };

    public static TwoWayFunction<String, String> STRING_CONVERTER= new TwoWayFunction<>() {
        @Override
        public String convert(String s) {
            return s;
        }

        @Override
        public String revert(String s) {
            return s;
        }
    };
}
