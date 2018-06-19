name := "lutecia"

version := "0.1"

scalaVersion := "2.12.6"

enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq("lutecia.*")

OsgiKeys.importPackage := Seq("*;resolution:=optional")

OsgiKeys.privatePackage := Seq("*")

OsgiKeys.requireCapability := """osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.8))""""

libraryDependencies += "org.scala-graph" %% "graph-core" % "1.12.5"

libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"
