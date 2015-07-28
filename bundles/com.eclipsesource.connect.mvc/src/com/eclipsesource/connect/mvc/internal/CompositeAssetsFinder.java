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
package com.eclipsesource.connect.mvc.internal;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.BundleTracker;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.google.common.base.Splitter;

public class CompositeAssetsFinder extends BundleTracker<Object>implements AssetsFinder {

  private final List<BundleAssetsFinder> finders;

  public CompositeAssetsFinder() {
    this( FrameworkUtil.getBundle( CompositeAssetsFinder.class ).getBundleContext() );
  }

  CompositeAssetsFinder( BundleContext context ) {
    super( context, Bundle.STARTING | Bundle.STOPPING | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED, null );
    finders = new ArrayList<>();
  }

  @Override
  public AssetsResult find( String name ) {
    checkArgument( name != null, "Name must not be null" );
    for( BundleAssetsFinder finder : finders ) {
      AssetsResult result = finder.find( name );
      if( result != null ) {
        return result;
      }
    }
    return null;
  }

  void activate( ComponentContext context ) {
    Bundle[] bundles = context.getBundleContext().getBundles();
    for( Bundle bundle : bundles ) {
      addingBundle( bundle, new BundleEvent( BundleEvent.INSTALLED, bundle ) );
    }
    open();
  }

  void deactivate() {
    close();
    finders.clear();
  }

  @Override
  public Object addingBundle( Bundle bundle, BundleEvent event ) {
    String assets = bundle.getHeaders().get( "Assets" );
    if( assets != null ) {
      Splitter.on( "," ).split( assets ).forEach( entry -> finders.add( new BundleAssetsFinder( bundle, entry ) ) );
    }
    return super.addingBundle( bundle, event );
  }

  @Override
  public void removedBundle( Bundle bundle, BundleEvent event, Object object ) {
    if( event.getType() == BundleEvent.UNINSTALLED ) {
      for( Iterator<BundleAssetsFinder> iterator = finders.iterator(); iterator.hasNext(); ) {
        BundleAssetsFinder finder = iterator.next();
        Bundle finderBundle = finder.getBundle();
        if( bundle.getBundleId() == finderBundle.getBundleId() ) {
          iterator.remove();
        }
      }
    }
    super.removedBundle( bundle, event, object );
  }
}
