# EasyJobTemplating _4_ Jenkins

Welcome to the _EasyJobTemplating_ project!

Want to learn further on what this project is about exactly? <ins>Keep reading!</ins>

- [Motivation](#motivation)
- [Purpose](#purpose)
- [Architecture](#architecture)
- [Directory Structure](#directory-structure)
- [Jenkins Plugins and Languages](#jenkins-plugins-and-languages)
    - [Jenkins DSL Under The Hood](#jenkins-dsl-under-the-hood)
    - [Groovy](#groovy)
- [Definition Job](#definition-job)


<br />

## Motivation

From long time ago, I've felt the necessity of automating the way we set up _repetitive_ jobs in Jenkins … and by _repetitive_ jobs I mean those that do the exact same thing but for different teams, departments, business units, and/or groups.

You may think: If they do the exact same thing, why can't you group them in a single job?

Short answer is:

* _Access control_: Not every member of a team needs or would be allowed to run certain jobs, or even make changes on jobs. So, by having separate job per team, organizations are able to put in place proper access control policies for them.
  
* _Customizations_: It'd be nice if you'd be able to make the jobs look different, or make them display different text while running the same logic begind the scenes, wouldn't it?

... <ins>Keep reading!</ins>

<br />

## Purpose

The _EasyJobTemplating 4 Jenkins_ project aims to ease the management of dozens, or hundreds, or even thousands of Jenkins jobs by setting up:

* A single Jenkins DSL script.
* A single Jenkins Pipeline script.
* And, a single Jenkins [Definition job](#definition-job) _per team / group / business unit / department_.

This one single _Definition job_ is all you need to get your teams up to speed.

But, once again, how does all this work? ... <ins>Keep reading!</ins>

<br />

## Architecture

As previously stated, _EasyJobTemplating_ is made up of:

(a) A _Definition Job_ - This is, in reality, a `vars` file containing the settings the target job will be created as. Through these settings (or _variables_), _EasyJobTemplating_ will know what Jenkins project to use, which DSL script to run, and which Pipeline script to add in.
> To clarify: Definition Jobs do not contain code of any kind, just settings that define a job.
>                 The sole existence of this file defines a job to create.

(b) A _single Jenkins DSL_ script - This is a regular Jenkins DSL script with some `Groovy` code embeeded to process the settings in the _Definition Job_.

(c) A _single Jenkins Pipeline_ script - This is a _Declarative_ Jenkins Pipeline that also processes the settings in the _Definition Job_.
<br />
<br />

But, ***there's also one last component that orchestrates the whole templating:***

(d) `job_builder.groovy` - This is a _Groovy_ script that gets called by the _seed_ job. It gets a list of the _Definition Jobs_ to process from the _seed_ job, and starts the job provisioning.

<br />
Take a quick a this diagram:

<br />
<br />

<p align="center">
  <img width="574" height="524" src="docs/images/EasyJobTemplating4Jenkins-Architecture.png">
</p>

<br />

(1) The _seed_ job (a) is run with a list of _Definition Jobs_ as parameters.

(2) The _Groovy_ script (d) gets called by the _seed_ job:
* It gets parameters from the _seed_ job as input.
* It iterates through the list of _Definition Jobs_, and loads them. Since they're just variables, these variables tells (d) which Jenkins project (i.e. `JOB_PARENT_PROJECT` variable) it belongs to, among other settings.
* By identifying the parent project, (d) also loads the parent project's `vars` file which contains the global settings for the project. Each _Definition Job_ is just sub-set of its parent project's `vars` file, and if some variables match between the two, the ones in the _Definition Job_ takes precedence. In other words, this is a mechanism to override global settings.
  
(3) The _DSL_ script (b) gets called by the _Groovy_ script. The _DSL_ script is the one that actually builds the target Jenkins job. It also loads the _Declarative Pipeline_ script (c) to embeed it into the newly-built target job.

<br />

## Directory Structure

<br />

## Jenkins Plugins and Languages

<br />
### Jenkins DSL Under The Hood

This project is based on [Jenkins DSL plugin](https://plugins.jenkins.io/job-dsl/)

Therefore, you'll need to set up a seed job, or a series of seed jobs, to create your team's target jobs.

I'll show you exactly how to do it later on this documentation.

<br />
### Groovy

<br />
## Definition Job