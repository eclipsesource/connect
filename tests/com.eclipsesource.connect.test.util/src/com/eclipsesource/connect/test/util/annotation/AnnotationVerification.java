/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.test.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class AnnotationVerification<T> {

  private Optional<MethodTracker<T>> tracker;
  private TrackingAnswer answer;
  private T instance;

  AnnotationVerification( T instance, Optional<MethodTracker<T>> tracker, TrackingAnswer answer ) {
    this.instance = instance;
    this.tracker = tracker;
    this.answer = answer;
  }

  public <A extends Annotation> AnnotationValidation<A> hasAnnotation( Class<A> annotationType ) {
    track();
    return new AnnotationValidation<>( assertInvocations( annotationType, true ).get() );
  }

  public <A extends Annotation> void doesNotHasAnnotation( Class<A> annotationType ) {
    track();
    assertInvocations( annotationType, false );
  }

  private void track() {
    try {
      if( tracker.isPresent() ) {
        tracker.get().track( instance );
      }
    } catch( Throwable doesNotMatter ) {
      // exception do not matter because we only want to get the method to ensure annotations
    }
  }

  private <A extends Annotation> Optional<A> assertInvocations( Class<A> annotationType, boolean required ) {
    Optional<A> result = Optional.empty();
    List<Method> invocations = answer.getInvocations();
    assertInvocationSize( invocations );
    for( Method invocation : invocations ) {
      result = assertMethodInvocation( invocation, annotationType, required );
    }
    return result;
  }

  private void assertInvocationSize( List<Method> invocations ) {
    if( invocations.isEmpty() ) {
      throw new AssertionError( "No method invocations tracked to assert Annotation" );
    }
    if( invocations.size() > 1 ) {
      throw new AssertionError( "To many method invocations. Supported is only one atm" );
    }
  }

  private <A extends Annotation> Optional<A> assertMethodInvocation( Method method, Class<A> annotationType, boolean required ) {
    Optional<A> result = Optional.empty();
    Annotation[] annotations = method.getAnnotations();
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == annotationType ) {
        result = Optional.of( annotationType.cast( annotation ) );
      }
    }
    assertAnnotation( method, annotationType, required, result.isPresent() );
    return result;
  }

  private <A extends Annotation> void assertAnnotation( Method method, Class<A> annotationType, boolean required, boolean present ) {
    if( required && !present ) {
      throw new AssertionError( createMissingErrorMessage( method, annotationType ) );
    }
    if( !required && present ) {
      throw new AssertionError( createNotWantedErrorMessage( method, annotationType ) );
    }
  }

  private <A extends Annotation> String createMissingErrorMessage( Method method, Class<A> annotationType ) {
    return "Method \"" + method.getName() + "\" does not has Annotation \""
           + annotationType.getSimpleName() + "\" on type "
           + instance.getClass().getSuperclass().getName();
  }

  private <A extends Annotation> String createNotWantedErrorMessage( Method method, Class<A> annotationType ) {
    return "Method \"" + method.getName() + "\" has illegal Annotation \""
        + annotationType.getSimpleName() + "\" on type "
        + instance.getClass().getSuperclass().getName();
  }


  <A extends Annotation> AnnotationValidation<A> hasAnnotationOnType( Class<A> annotationType ) {
    A annotation = instance.getClass().getSuperclass().getAnnotation( annotationType );
    if( annotation == null ){
      throw new AssertionError( createTypeMissingErrorMessage( annotationType ) );
    }
    return new AnnotationValidation<>( annotation );
  }

  <A extends Annotation> void doesNotHasAnnotationOnType( Class<A> annotationType ) {
    A annotation = instance.getClass().getSuperclass().getAnnotation( annotationType );
    if( annotation != null ){
      throw new AssertionError( createTypeNotWantedErrorMessage( annotationType ) );
    }
  }

  private <A extends Annotation> String createTypeMissingErrorMessage( Class<A> annotationType ) {
    return "Annotation \""
           + annotationType.getSimpleName() + "\" not present on type "
           + instance.getClass().getSuperclass().getName();
  }

  private <A extends Annotation> String createTypeNotWantedErrorMessage( Class<A> annotationType ) {
    return "Annotation \""
           + annotationType.getSimpleName() + "\" is illegaly on type "
           + instance.getClass().getSuperclass().getName();
  }

}