package com.octopus_tech.goc.action.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.google.protobuf.Option;
import com.octopus_tech.goc.crud.CategoryQ;
import com.octopus_tech.goc.crud.ClickRecordQ;
import com.octopus_tech.goc.crud.ItemQ;
import com.octopus_tech.goc.crud.LevelQ;
import com.octopus_tech.goc.model.Category;
import com.octopus_tech.goc.model.ClickRecord;
import com.octopus_tech.goc.model.Item;
import com.octopus_tech.goc.model.Level;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;

public class MarkClickAction extends BasicApiAction {
	private static final long serialVersionUID = 8058318275205405993L;

	private ItemQ itemQ;
	private ClickRecordQ clickRecordQ;

	public Integer itemId;
	public String method;
	
	private LevelQ levelQ;
	private CategoryQ categoryQ;

	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if ("markClick".equals(method)) {
			return addClickRecord(dbHelper, logger, responseMap);
		} else if ("countClickRecord".equals(method)) {
			return countClickRecord(dbHelper, logger, responseMap);
		} else if ("listClickRecord".equals(method)) {
			return listClickRecord(dbHelper, logger, responseMap);
		} else {
			return ERROR;
		}
	}
	
	public String addClickRecord() {
		return SUCCESS;
	}
	
	protected String addClickRecord(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if (!isAuthenticated()) {
			return ERROR;
		}

		User user = (User) session.get("user");

		responseMap.put("code", 1);

		if ("POST".equals(request.getMethod())) {
			Item item = itemQ.getById(itemId).get();

			if (item != null) {
				ClickRecord clickRecord = new ClickRecord();
				clickRecord.setItem(item);
				clickRecord.setUser(user);
				clickRecord.setClickTime(new Date());

				clickRecordQ.add(clickRecord);

				responseMap.put("code", 0);
				responseMap.put("click_record", clickRecord);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		return SUCCESS;
	}

	protected String countClickRecord(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if (!isAuthenticated()) {
			return ERROR;
		}

		List<ClickRecord> clickRecordList = clickRecordQ.listAll();
		Map<Integer, Integer> clickRecordCountMap = new HashMap<>();

		clickRecordList.forEach(clickRecord -> {
			Integer itemId = clickRecord.getItem().getId();
			Integer count = clickRecordCountMap.get(itemId);
			if (count == null) {
				count = 0;
			}
			count++;
			clickRecordCountMap.put(itemId, count);
		});

		responseMap.put("code", 0);
		responseMap.put("click_record_count", clickRecordCountMap);

		return SUCCESS;
	}

	protected String listClickRecord(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception {
		if (!isAuthenticated()) {
			return ERROR;
		}

		List<ClickRecord> clickRecordList;
		if (itemId != null) {
			clickRecordList = clickRecordQ.listByItem_Id(itemId);
		} else {
			clickRecordList = clickRecordQ.listAll();
		}
		Map<Integer, List<ClickRecord>> clickRecordListMap = new HashMap<>();

		clickRecordList.forEach(clickRecord -> {
			Integer itemId = clickRecord.getItem().getId();
			List<ClickRecord> clickRecordListByItemId = clickRecordListMap.get(itemId);
			if (clickRecordListByItemId == null) {
				clickRecordListByItemId = new ArrayList<>();
			}
			clickRecordListByItemId.add(clickRecord);
			clickRecordListMap.put(itemId, clickRecordListByItemId);
		});

		responseMap.put("code", 0);
		responseMap.put("click_records", clickRecordListMap);

		return SUCCESS;
	}
}