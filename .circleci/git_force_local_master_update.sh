#!/usr/bin/env bash
###############################################################################
#
#       Script for fix Sonar analysis issue for CircleCI builds.
#
#       Created:  Dmitrii Gusev, 04.01.2020
#       Modified:
#
###############################################################################

if [[ ${CIRCLE_BRANCH} != "master" ]]; then
    git branch -f master origin/master
fi
