/*
 * Copyright © 2015 - 2017 Lightbend, Inc. <http://www.lightbend.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


inThisBuild(List(
  organization := "com.lightbend.paradox",
  licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"),
  scalaVersion := "2.13.1",
  organizationName := "lightbend",
  organizationHomepage := Some(url("https://lightbend.com/")),
  homepage := Some(url("https://developer.lightbend.com/docs/paradox/current/")),
  scmInfo := Some(ScmInfo(url("https://github.com/lightbend/paradox"), "git@github.com:lightbend/paradox.git")),
  developers := List(
    Developer("pvlugter", "Peter Vlugter", "@pvlugter", url("https://github.com/pvlugter")),
    Developer("eed3si9n", "Eugene Yokota", "@eed3si9n", url("https://github.com/eed3si9n"))
  ),
  description := "Paradox is a markdown documentation tool for software projects.",
))

lazy val paradox = project
  .in(file("."))
  .aggregate(core, testkit, tests, themes, docs)
  .settings(
    publish / skip := true
  )

lazy val core = project
  .in(file("core"))
  .settings(
    name := "paradox",
    libraryDependencies ++= Seq(
                           "org.pegdown"     % "pegdown"        % "1.6.0",
                           "org.parboiled"   % "parboiled-java" % "1.3.0", // overwrite for JDK10 support
      "org.antlr"         % "ST4"        % "4.1"
                         ),
    parallelExecution in Test := false
  )

lazy val testkit = project
  .in(file("testkit"))
  .dependsOn(core)
  .settings(
    name := "testkit",
    libraryDependencies ++= Seq(
      "net.sf.jtidy"      % "jtidy"      % "r938"
    )
  )

lazy val tests = project
  .in(file("tests"))
  .dependsOn(core, testkit)
  .settings(
    name := "tests",
    libraryDependencies ++= Seq(
      "org.scalatest"    %% "scalatest"  % "3.1.1"
    ),
    publish / skip := true
  )

lazy val themes = (project in file("themes"))
  .settings(
    publish / skip := true
  )

lazy val docs = (project in file("docs"))
  .settings(
    name := "paradox docs",
    publish / skip := true
  )

addCommandAlias("verify", ";test:compile ;compile:doc ;test ;scripted ;docs/paradox")
