var fs = require( "fs-extra" ),
    path = require( "path" )

module.exports = (function() {

  var resultCallback;

  var createBundle = function( config ) {
    createProjectFolders( config.bundle.location, config.bundle.name );
    fs.mkdirsSync( path.join( config.bundle.location, config.bundle.name, "OSGI-INF" ) );
    writeProjectFiles( config.bundle.location, config.bundle );
    writeManifest( config.bundle.location, config.bundle );
    addToParentPom( config, config.bundle );
    addToFeature( config );
  };

  var createTest = function( config ) {
    createProjectFolders( config.test.location, config.test.name );
    writeProjectFiles( config.test.location, config.test );
    writeManifest( config.test.location, config.test );
    addToParentPom( config, config.test );
  };

  var createProjectFolders = function( parent, name ) {
    if( fs.existsSync( path.join( parent, name ) ) ) {
      log( { error : "Project " + name + " already exists in " + parent } );
    }
    fs.mkdirsSync( path.join( parent, name ) );
    fs.mkdirsSync( path.join( parent, name, "src" ) );
    fs.mkdirsSync( path.join( parent, name, "META-INF" ) );
    fs.mkdirsSync( path.join( parent, name, ".settings" ) );
    log( { success : "Created project folders for " + name + " in " + parent } );
  };

  var writeProjectFiles = function( parent, project ) {
    if( typeof project.files !== "undefined" ) {
      Object.keys( project.files ).forEach( function( key ) {
        fs.writeFileSync( path.join( parent, project.name, key ), project.files[ key ], "utf-8" );
        log( { success : "Wrote project file " + key + " in project " + project.name } );
      });
    }
  };

  var writeManifest = function( parent, project ) {
    var manifest = project.manifest;
    if( typeof manifest !== "undefined" ) {
      Object.keys( manifest ).forEach( function( key ) {
        fs.appendFileSync( path.join( parent, project.name, "META-INF", "MANIFEST.MF" ), key + ": " + manifest[ key ] + "\n", "utf-8" );
      });
      log( { success : "Wrote manifest file in project " + project.name } );
    }
  };

  var addToParentPom = function( config, project ) {
    if( typeof config.pom !== "undefined" ) {
      if( !fs.existsSync( path.join( config.pom, "pom.xml" ) ) ) {
        log( { error : "Parent pom " + path.join( config.pom, "pom.xml" ) + " does not exist" } );
      }
      writeToParentPom( config, project );
    }
  };

  var writeToParentPom = function( config, project ) {
    var projectPath = path.relative( config.pom, project.location );
    var i = 0;
    var lines = fs.readFileSync( path.join( config.pom, "pom.xml" ) ).toString().split( "\n" );
    for( i = 0; i < lines.length; i++ ) {
      if( lines[ i ].indexOf( "</modules>" ) >= 0 ) {
        lines.splice( i, 0, "    <module>" + path.join( projectPath, project.name ) + "</module>" );
        break;
      }
    }
    fs.writeFileSync( path.join( config.pom, "pom.xml" ), lines.join( "\n" ) );
    log( { success : "Added project " + project.name + " to parent pom " + path.join( config.pom, "pom.xml" ) } );
  };

  var addToFeature = function( config ) {
    if( typeof config.feature !== "undefined" ) {
      if( !fs.existsSync( path.join( config.feature ) ) ) {
        log( { error : "Feature " + path.join( config.feature ) + " does not exist" } );
      }
      writeToFeature( config );
    }
  };

  var writeToFeature = function( config ) {
    var i = 0;
    var lines = fs.readFileSync( path.join( config.feature ) ).toString().split( "\n" );
    for( i = 0; i < lines.length; i++ ) {
      if( lines[ i ].indexOf( "</feature>" ) >= 0 ) {
        lines.splice( i, 0, "   <plugin\n         id=\"" + config.bundle.name + "\"\n         download-size=\"0\"\n         install-size=\"0\"\n         version=\"0.0.0\"\n         unpack=\"false\"\/>\n" );
        break;
      }
    }
    fs.writeFileSync( path.join( config.feature ), lines.join( "\n" ) );
    log( { success : "Added project " + config.bundle.name + " to feature " + path.join( config.feature ) } );
  };
  
  var log = function( result ) {
    if( typeof resultCallback !== "undefined" ) {
      resultCallback( result );
    } else {
      if( result.success ) {
        console.log( "SUCCESS: " + result.success );
      } else {
        console.log( "ERROR: " + result.error );
      }
    }
  };

  return {
    create : function( config, callback ) {
      resultCallback = callback;
      createBundle( config );
      if( typeof config.test.location !== "undefined" ) {
        createTest( config );
        log( { success : "All Done! Bundle \"" + config.bundle.name + "\" and tests \"" + config.test.name + "\" successfully created." } );
      } else {
        log( { success : "All Done! Bundle \"" + config.bundle.name + "\" successfully created." } );
      }
    }
  }

}());