Для установки ворднета выполнить команды

1. sudo apt-get install tcl-dev
2. sudo apt-get install tk-dev
3. ./configure
4. в файле src/stub.c перед #include<tcl.h> добавить:

        #ifndef USE_INTERP_RESULT
        #define  USE_INTERP_RESULT 1
        #endif

5. sudo make
6. sudo make (хз почему именно два раза, но надо два раза)
7. sudo make install
8. profit
