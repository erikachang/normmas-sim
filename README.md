## NormMAS Simulator

This is the code repository for the NormMAS project, an implementation of a framework for norm monitoring and enforcement in Multi-Agent Systems using [Jason][2] and [CArtAgO][3].

Our published paper [1], in which we discuss the framework's architecture and functionality, is available [here](http://www.meneguzzi.eu/felipe/pubs/coin-simulating-norms-2015.pdf).

#### Differences between branches

As of now, there are two branches in this repository.

The **master-old** branch contains the first version of our implementation, which is the one we used for the experiments detailed in our paper. It is no longer under development, and is only maintained as a reference for the paper's contents.

The **normmas-extended** branch contains an extension of the first implementation, which uses a more up-to-date version of Jason and CArtAgO integrated by [JaCaMo][4]. This extension also includes a per-artefact action capturing mechanism and a few changes in the proposed normative simulation. The extension for this scenario is, however, still under development, and therefore there are no results to discuss yet.

#### References

[1] "CHANG, Stephan and MENEGUZZI, Felipe. Simulating Normative Behaviour in Multi-Agent Environments using Monitoring Artefacts, In 17th International Workshop on Coordination, Organisations, Institutions and Norms (COIN 2015) @AAMAS, Istanbul, Turkey, 2015."
[2]: http://jason.sf.net/ "Programming Multi-Agent Systems with Jason"
[3]: http://cartago.sf.net/ "Programming Multi-Agent Environments with CArtAgO"
[4]: http://jacamo.sf.net/ "Multi-agent Programming with JaCaMo"

### License Info
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>
