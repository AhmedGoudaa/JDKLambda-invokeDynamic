package org.gouda;


import org.gouda.BiConsumer.ObjBoolConsumer;
import org.gouda.BiConsumer.ObjByteConsumer;
import org.gouda.BiConsumer.ObjCharConsumer;
import org.gouda.BiConsumer.ObjCollectionConsumer;
import org.gouda.BiConsumer.ObjFloatConsumer;
import org.gouda.BiConsumer.ObjShortConsumer;
import org.gouda.BiConsumer.ObtMapConsumer;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
final public class DynamicMapping {

  private static final Pattern              FIELD_SEPARATOR = Pattern.compile("\\.");
  private static final MethodHandles.Lookup LOOKUP          = MethodHandles.lookup();

  private static final WeakHashMap<String, Tuple<Function, BiConsumer>> CACHE = new WeakHashMap<>();


  private DynamicMapping() {
  }

  public static <S, D> D map(S source, D destination, Rules rules) {

    for (Rule rule : rules.getRules()) {
      destination = map(source, destination, rule);
    }
    return destination;
  }

  public static <S, D> D map(S source, D destination, Rule rule) {

    Tuple<Function, BiConsumer> getterAndSetter =
            CACHE.computeIfAbsent(rule.getName(), k -> {

              Function   getFunction    = createGetFunction(source.getClass(), FIELD_SEPARATOR.split(rule.getSourcePath()))._2;
              BiConsumer setterConsumer = createSetterConsumer(destination.getClass(), FIELD_SEPARATOR.split(rule.getDestinationPath()), rule.getMapperFunction());

              return Tuple.of(getFunction, setterConsumer);
            });

    getterAndSetter._2.accept(destination, getterAndSetter._1.apply(source));

    return destination;

  }


  /**
   * Create get function for a class based on the Class name and the method path eg: getAddress.getStreetName.getStName.
   *
   * @param aClass
   * @param getMethodPath
   * @return
   */
  private static Tuple<Class, Function> createGetFunction(Class aClass, String[] getMethodPath) {
    return Stream.of(getMethodPath)
            .reduce(Tuple.<Class, Function>of(aClass, Function.identity()),
                    (tuple, getMethodName) -> {
                      Tuple<? extends Class, Function> functionTuple = createFunction(getMethodName, tuple._1);
                      return Tuple.of(functionTuple._1, tuple._2.andThen(functionTuple._2));
                    },
                    (prev, next) -> next);
  }

  private static Tuple<Class, Function> createFunction(String fieldName, Class<?> javaBeanClass) {
    return Stream.of(javaBeanClass.getDeclaredMethods())
            .filter(method -> method.getName().endsWith(fieldName))
            .map(DynamicMapping::createTupleWithReturnTypeAndGetter)
            .findFirst()
            .orElseThrow(IllegalStateException::new);
  }

  private static Tuple<Class, Function> createTupleWithReturnTypeAndGetter(Method getterMethod) {
    try {
      return Tuple.of(
              getterMethod.getReturnType(),
              (Function) createGetterCallSite(LOOKUP.unreflect(getterMethod)).getTarget().invokeExact()
      );
    } catch (Throwable e) {

      throw new IllegalArgumentException("Can't create lambda for getterMethod => " + getterMethod.getName(), e);
    }
  }

  private static CallSite createGetterCallSite(MethodHandle getterMethodHandle) throws LambdaConversionException {
    return LambdaMetafactory.metafactory(LOOKUP, "apply",
            MethodType.methodType(Function.class),
            MethodType.methodType(Object.class, Object.class),
            getterMethodHandle, getterMethodHandle.type());
  }


  private static BiConsumer createSetterConsumer(Class aClass, String[] setMethodPath, Function mappingFunc) {

    if (setMethodPath.length > 1) {

      String[]               getPath          = Arrays.copyOfRange(setMethodPath, 0, setMethodPath.length - 1);
      Tuple<Class, Function> getFunctionTuple = createGetFunction(aClass, getPath);

      return createSetterFunction(setMethodPath[setMethodPath.length - 1], getFunctionTuple._1, getFunctionTuple._2, mappingFunc);
    }

    return createSetterFunction(setMethodPath[0], aClass, Function.identity(), mappingFunc);
  }

  private static BiConsumer createSetterFunction(String fieldName, Class<?> javaBeanClass, Function lastGetterForSetter, Function mappingFunc) {
    return Stream.of(javaBeanClass.getDeclaredMethods())
            .filter(method -> method.getName().endsWith(fieldName) && method.getParameterCount() == 1)
            .map(method -> createSetterBiConsumer(method, lastGetterForSetter, mappingFunc))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
  }

  private static BiConsumer createSetterBiConsumer(Method method, Function getterBeforeSetter, Function mappingFunc) {
    try {

      MethodHandle methodHandle  = LOOKUP.unreflect(method);
      Class<?>     parameterType = method.getParameterTypes()[0];
      if (parameterType.isPrimitive()) {

        if (parameterType == double.class) {

          final ObjDoubleConsumer consumer = (ObjDoubleConsumer) createSetterCallSite(methodHandle, ObjDoubleConsumer.class, double.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (double) b);
          };

        } else if (parameterType == int.class) {

          final ObjIntConsumer consumer = (ObjIntConsumer) createSetterCallSite(methodHandle, ObjIntConsumer.class, int.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (int) b);
          };

        } else if (parameterType == long.class) {

          final ObjLongConsumer consumer = (ObjLongConsumer) createSetterCallSite(methodHandle, ObjLongConsumer.class, long.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (long) b);
          };

        } else if (parameterType == boolean.class) {

          final ObjBoolConsumer consumer = (ObjBoolConsumer) createSetterCallSite(methodHandle, ObjLongConsumer.class, boolean.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (boolean) b);
          };

        } else if (parameterType == float.class) {

          final ObjFloatConsumer consumer = (ObjFloatConsumer) createSetterCallSite(methodHandle, ObjLongConsumer.class, float.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (float) b);
          };

        } else if (parameterType == char.class) {

          final ObjCharConsumer consumer = (ObjCharConsumer) createSetterCallSite(methodHandle, ObjCharConsumer.class, char.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (char) b);
          };

        } else if (parameterType == short.class) {

          final ObjShortConsumer consumer = (ObjShortConsumer) createSetterCallSite(methodHandle, ObjShortConsumer.class, short.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(obj, (short) b);
          };

        } else {
          final ObjByteConsumer consumer = (ObjByteConsumer) createSetterCallSite(methodHandle, ObjByteConsumer.class, byte.class).getTarget().invokeExact();
          return (a, b) -> {
            Object obj = getterBeforeSetter.apply(a);
            consumer.accept(a, (byte) b);
          };

        }
      } else if (parameterType == List.class || parameterType == ArrayList.class) {

        final ObjCollectionConsumer biConsumer = (ObjCollectionConsumer) createSetterCallSite(methodHandle, ObjCollectionConsumer.class, Collection.class).getTarget().invokeExact();

//        final Class<?> typeParam = (Class<?>) ((ParameterizedType) method.getParameters()[0].getParameterizedType()).getActualTypeArguments()[0];

        return (a, b) -> {
          Object obj = getterBeforeSetter.apply(a);

          if (b == null) {
            biConsumer.accept(obj, null);
            return;
          }

          if (mappingFunc == null) {
            // the types r the same
            biConsumer.accept(obj, (List) b);
            return;
          }

          List<?> result = getCollectionMapping((List) b, mappingFunc, () -> new ArrayList<>());
          biConsumer.accept(obj, result);

        };
      } else if (parameterType == Set.class || parameterType == HashSet.class) {

        final ObjCollectionConsumer biConsumer = (ObjCollectionConsumer) createSetterCallSite(methodHandle, ObjCollectionConsumer.class, Collection.class).getTarget().invokeExact();

        return (a, b) -> {
          Object obj = getterBeforeSetter.apply(a);

          if (b == null) {
            biConsumer.accept(obj, null);
            return;
          }

          if (mappingFunc == null) {
            // the types r the same
            biConsumer.accept(obj, (List) b);
            return;
          }

          Set<?> result = getCollectionMapping((Set) b, mappingFunc, () -> new HashSet<>());
          biConsumer.accept(obj, result);


        };
      } else if (parameterType == Map.class || parameterType == HashMap.class || parameterType == ConcurrentHashMap.class) {

        final ObtMapConsumer biConsumer = (ObtMapConsumer) createSetterCallSite(methodHandle, ObtMapConsumer.class, Map.class).getTarget().invokeExact();

        return (a, b) -> {
          Object obj = getterBeforeSetter.apply(a);

          if (b == null) {
            biConsumer.accept(obj, null);
            return;
          }

          if (mappingFunc == null) {  // the types r the same

            biConsumer.accept(obj, (Map) b);
            return;
          }
          // the types r diff and there is a mapping function

          Map<? extends Comparable, ?> result =
                  (Map<? extends Comparable, ?>) ((Map) b)
                          .entrySet()
                          .stream()
                          .map(mappingFunc)
                          .collect(Collectors.toMap((Map.Entry entry) -> entry.getKey(),
                                  (Map.Entry entry) -> entry.getValue(), (o, o2) -> o2));
          biConsumer.accept(obj, result);


        };
      }

      final BiConsumer biConsumer = (BiConsumer) createSetterCallSite(methodHandle, BiConsumer.class, Object.class).getTarget().invokeExact();
      return (a, b) -> {
        Object obj = getterBeforeSetter.apply(a);
        biConsumer.accept(obj, b);
      };
    } catch (Throwable e) {
      throw new IllegalArgumentException("Can't create lambda for setterMethod => " + method.getName(), e);
    }
  }

  private static <C extends Collection> C getCollectionMapping(Collection input, Function mappingFunc, Supplier<C> supplier) {

    if (mappingFunc == null) {
      throw new IllegalArgumentException("Needs Static mapper function to map different types, function can't be null");
    }

    return (C) input
            .stream()
            .map(mappingFunc)
            .collect(Collectors.toCollection(supplier));
  }

  private static CallSite createSetterCallSite(MethodHandle setterMethodHandle, Class<?> biConsumerClass, Class<?> parameterType) throws
          LambdaConversionException {
    return LambdaMetafactory.metafactory(LOOKUP,
            "accept",
            MethodType.methodType(biConsumerClass),
            MethodType.methodType(void.class, Object.class, parameterType),
            setterMethodHandle,
            setterMethodHandle.type());
  }

}
