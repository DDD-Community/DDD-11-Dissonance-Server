package com.dissonance.itit.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.function.CommonFunctionFactory;
import org.hibernate.type.StandardBasicTypes;

public class MariaDBFullTextDialect extends MariaDBDialect {
	@Override
	public void initializeFunctionRegistry(FunctionContributions functionContributions) {
		super.initializeFunctionRegistry(functionContributions);

		CommonFunctionFactory commonFunctionFactory = new CommonFunctionFactory(functionContributions);
		commonFunctionFactory.windowFunctions();
		commonFunctionFactory.hypotheticalOrderedSetAggregates_windowEmulation();

		// Full-Text Search 관련 함수 등록
		functionContributions.getFunctionRegistry().registerPattern(
			"match_against",
			"MATCH(?1, ?2) AGAINST (?3 IN BOOLEAN MODE)",
			functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)
		);
	}
}
