package distributed.transaction.reflect;

import distributed.transaction.model.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflect {

    public static void main(String[] args) {
        print(new User());
    }

    /**
     * 反射获取方法和field
     *
     * @param model
     */
    public static void print(Object model) {
        Class cls = model.getClass();
        Field[] fields = cls.getDeclaredFields();
        System.out.println("###################### " + model.getClass().getName() + " ####################");
        for (Field field : fields) {
            char[] buffer = field.getName().toCharArray();
            buffer[0] = Character.toUpperCase(buffer[0]);
            String mothodName = "get" + new String(buffer);
            try {
                Method method = cls.getDeclaredMethod(mothodName);
                Object resutl = method.invoke(model, null);
                System.out.println(field.getName() + ": " + resutl + ">>>mothodName:" + mothodName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("###################### End ####################");
    }
}
