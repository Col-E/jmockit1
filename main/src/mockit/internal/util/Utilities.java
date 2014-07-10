/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.state.*;

import org.jetbrains.annotations.*;

/**
 * Miscellaneous utility methods.
 */
public final class Utilities
{
   public static void ensureThatMemberIsAccessible(@NotNull AccessibleObject classMember)
   {
      if (!classMember.isAccessible()) {
         classMember.setAccessible(true);
      }
   }

   public static void ensureThatClassIsInitialized(@NotNull Class<?> aClass)
   {
      ExecutingTest executingTest = TestRun.getExecutingTest();
      boolean previousFlag = executingTest.setShouldIgnoreMockingCallbacks(true);

      try {
         Class.forName(aClass.getName(), true, aClass.getClassLoader());
      }
      catch (ClassNotFoundException ignore) {}
      catch (LinkageError e) {
         StackTrace.filterStackTrace(e);
         e.printStackTrace();
      }
      finally {
         executingTest.setShouldIgnoreMockingCallbacks(previousFlag);
      }
   }

   @NotNull
   public static Class<?> getClassType(@NotNull Type declaredType)
   {
      if (declaredType instanceof ParameterizedType) {
         return (Class<?>) ((ParameterizedType) declaredType).getRawType();
      }
      else if (declaredType instanceof TypeVariable) {
         Type firstBound = ((TypeVariable<?>) declaredType).getBounds()[0];
         return getClassType(firstBound);
      }

      return (Class<?>) declaredType;
   }

   public static boolean containsReference(@NotNull List<?> references, @Nullable Object toBeFound)
   {
      return indexOfReference(references, toBeFound) >= 0;
   }

   public static int indexOfReference(@NotNull List<?> references, @Nullable Object toBeFound)
   {
      for (int i = 0, n = references.size(); i < n; i++) {
         if (references.get(i) == toBeFound) {
            return i;
         }
      }

      return -1;
   }
}
