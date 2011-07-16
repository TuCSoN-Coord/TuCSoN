Multimedia demo description
---------------------

This application is an example of distributed master-worker applications.
An image is reconstructed in a window by means of an open set of collaborating
worker agents, each with the same capabilities, acting on different parts of
the image.
It's a typical example of objective coordination, in what the
coordination of worker agents is done outside the agent themselves, by means
of the default tuple centre programmed accordingly (with the spec.rsp
specification): agents are free to focus only on their task build a piece of
image, and can participate to the global goal without knowing details about
how their work will be integrated with other agents' work.
The demo is useful to show how to handle multimedia information exchange among
agents using tuple centre (and logic tuples in particular).

The society is composed by following agent/roles:

There is a Master agent, which is responsible
- to install the coordination specification in the default tuple centre
- to visualize current state of the gloabal work, which is current state
  of the full image to be reconstructed

Then there is an open set of Worker agents, which can be join anytime from
anywhere to the society, to contribute to the image construction.

Be sure to have the alice/tucson/examples package accessible from your path.

To spawn the master agent:
        java -cp .;tucson.jar alice.tucson.examples.multimedia.MasterAgent <TuCSoN node address>

The master will install the coordination specificiation of the default
tuple centre of the TuCSoN node specified. In the case that there are not
available TuCSoN node, you can always install one on localhost, by means of

        java -cp tucson.jar alice.tucson.service.Node

After the specification has been setup, the workers can be spawned.

To spawn a worker agent:
        java -cp .;tucson.jar alice.tucson.examples.multimedia.WorkerAgent <name>
where <name> is the name of the agent
es:
        java -cp .;tucson.jar alice.tucson.examples.multimedia.WorkerAgent worker-0

The agent asks the user the address of the TuCSoN node where the work
must be done and then start to work.

Obviously the target specified host must be a TuCSoN node, that is the
TuCSoN infrastructure must be installed and active.
