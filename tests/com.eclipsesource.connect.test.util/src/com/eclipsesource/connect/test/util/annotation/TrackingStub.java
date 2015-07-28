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
import java.util.Optional;


public class TrackingStub<T> {

  private T instance;
  private TrackingAnswer answer;

  TrackingStub( T instance, TrackingAnswer answer ) {
    this.instance = instance;
    this.answer = answer;
  }

  public AnnotationVerification<T> verifyThat( MethodTracker<T> tracker ) {
    return new AnnotationVerification<>( instance, Optional.of( tracker ), answer );
  }

  public <A extends Annotation> AnnotationValidation<A> verifyHasAnnotation( Class<A> annotationType ) {
    AnnotationVerification<T> verification = new AnnotationVerification<>( instance, Optional.empty(), answer );
    return verification.hasAnnotationOnType( annotationType );
  }

  public <A extends Annotation> void verifyDoesNotHaveAnnotation( Class<A> annotationType ) {
    AnnotationVerification<T> verification = new AnnotationVerification<>( instance, Optional.empty(), answer );
    verification.doesNotHasAnnotationOnType( annotationType );
  }

}