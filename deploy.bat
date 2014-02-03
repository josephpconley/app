call git add .
call git commit -am %1
call git checkout heroku
call git merge master
call cd C:\workspace\joec\scala\puzzles
call sbt package
call cd C:\workspace\joec
call cp scala/puzzles/target/scala-2.10/puzzles_2.10-0.1.jar toolbox/lib/puzzles_2.10-0.1.jar
call cd C:\workspace\joec\toolbox
call git add .
call git commit -am %1
call git push -f heroku HEAD:master
call git checkout master