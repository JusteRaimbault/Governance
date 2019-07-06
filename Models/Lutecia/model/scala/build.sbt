name := "lutecia"

version := "0.1"

scalaVersion := "2.12.6"

enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq("lutecia.*")

OsgiKeys.importPackage := Seq("*;resolution:=optional")

OsgiKeys.privatePackage := Seq("!scala.*,*")

OsgiKeys.requireCapability := """osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.8))""""

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("staging"),
  Resolver.mavenCentral
)

libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"
libraryDependencies += "org.openmole" %% "spatialdata" % "0.1-SNAPSHOT" //exclude("org.scala-graph","graph-core")
libraryDependencies += "org.scala-graph" %% "graph-core" % "1.12.5"

mainClass in (Compile,run) := Some("RunTest")
