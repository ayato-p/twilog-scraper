================
 Twilog scraper
================

これは `Twilog <http://twilog.org/>`_ をスクレイピングするためのライブラリ/クライアントツールです。

Warning
=======

`Skyscraper <https://github.com/nathell/skyscraper>`_ に依存していますが、僕の修正が取り込まれたバージョンがリリースされていないので Skyscraper の master を自力で ``lein install`` しないとこのツールはまともに動きません( 2 週間以内にはリリースされる気がするけど…)。

Usage
=====

As a library
------------

ライブラリとして使う場合は以下を ``project.clj`` の ``:dependencies`` へと追加

.. sourcecode:: clojure

  [twilog-scraper "0.1.0-SNAPSHOT"]



As a client tool
----------------

クライアントツールとして使う場合

.. sourcecode:: shell

  $ lein uberjar
  $ java -jar target/twilog-scraper.jar --help

License
=======

The MIT License (MIT)

Copyright (c) 2015 Ayato Nishimura, http://ayalog.com/

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
