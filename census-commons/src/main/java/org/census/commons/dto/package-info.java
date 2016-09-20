@org.hibernate.annotations.GenericGenerator(
        name = CS_ID_GENERATOR_NAME,
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = CS_ID_GENERATOR_SEQUENCE_NAME
                ),
                @org.hibernate.annotations.Parameter(
                        name = "initial_value",
                        value = "1"
                )
        })

package org.census.commons.dto;

import static org.census.commons.CensusDefaults.CS_ID_GENERATOR_NAME;
import static org.census.commons.CensusDefaults.CS_ID_GENERATOR_SEQUENCE_NAME;