@echo off
E:
cd E:\workspace\ec\wms\mybatis_generator
java -jar mybatis-generator-core-1.3.1.jar -configfile generatorConfig-oracle.xml -overwrite

pause