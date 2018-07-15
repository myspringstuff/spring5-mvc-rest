package guru.springfamework;

import guru.springfamework.annotation.IgnoredProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BeanSupport {

    public static void copyProperties(Object source, Object target, boolean ignoreNullProperties) {
        String[] ignoredProperties = getIgnoredProperties(source, ignoreNullProperties).toArray(new String[]{});
        BeanUtils.copyProperties(source, target, ignoredProperties);
    }


    private static Set<String> getIgnoredProperties(Object incoming, boolean ignoreNullProperties) {
        Set<String> ignoredProperties = new HashSet<>();

        Arrays.asList(incoming.getClass().getDeclaredFields())
                .forEach(field -> {
                    if (field.isAnnotationPresent(IgnoredProperty.class)) {
                        ignoredProperties.add(field.getName());
                    }
                });

        if (ignoreNullProperties) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(incoming);
            Arrays.asList(beanWrapper.getPropertyDescriptors()).forEach(pd -> {
                String propertyName = pd.getName();
                Object propertyValue = beanWrapper.getPropertyValue(propertyName);
                if (propertyValue == null) {
                    ignoredProperties.add(propertyName);
                }
            });
        }

        return ignoredProperties;
    }
}
