package com.octopus_tech.goc.action.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.crud.NewsQ;
import com.octopus_tech.goc.model.News;
import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.db.DBHelper;

public class NewsAction extends BasicApiAction
{
	private static final long serialVersionUID = 8930160043856206840L;

	private NewsQ newsQ;

	public String _action = "";
	public Integer id;


	public String title;
	public String content;
	public String status;
	public String lang;
	public Integer order;
	public Long release;

	@Override
	protected String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception
	{
		if(!isAuthenticated())
		{
			return ERROR;
		}

		User user = (User)session.get("user");

		responseMap.put("code", 1);

		if("GET".equals(request.getMethod()))
		{
			if(id != null && id.intValue() > 0)
			{
				Optional<News> news = newsQ.getById(id.intValue());
				if(news.isPresent())
				{
					if(Objects.equals(news.get().getStatus(), News.STATUS_SHOW))
					{
						if(news.get().getSchool().getId() == user.getSchool().getId())
						{
							responseMap.put("code", 0);
							responseMap.put("news", news.get());
						}
					}
				}
			}
			else
			{
				List<News> newsList = newsQ.listBySchool(user.getSchool());
				responseMap.put("code", 0);
				responseMap.put("news", newsList);
			}
		}
		/*
		else if("POST".equals(request.getMethod()))
		{
			if(id == null)
			{
				if(user.getAdminLevel() < 1)
				{
					responseMap.put("code", 2);
					return;
				}

				if(title == null || title.length() == 0)
				{
					return;
				}

				if(content == null || content.length() == 0)
				{
					return;
				}
				if(status == null || !Arrays.asList(News.STATUS_ALL).contains(status))
				{
					return;
				}

				if(lang == null || lang.length() == 0)
				{
					lang = "Zh";
				}

				if(order == null)
				{
					order = 0;
				}


				News news = new News();
				news.setTitle(title);
				news.setContent(content);
				news.setStatus(status);
				news.setAdd(Epoch.now());
				news.setLastmod(Epoch.now());
				news.setUser(user);
				news.setSchool(user.getSchool());
				news.setLang(lang);
				news.setOrder(order.intValue());

				if(release != null)
				{
					news.setRelease(new Epoch(release));
					if(new Epoch(release.intValue()).before(Epoch.now()))
					{
						news.setRelease(Epoch.now());
					}
				}

				newsQ.add(news);

				responseMap.put("code", 0);
				responseMap.put("news", news);
			}
			else
			{
				if(user.getAdminLevel() < 1)
				{
					responseMap.put("code", 2);
					return;
				}

				if(id <= 0)
				{
					responseMap.put("code", 3);
					return;
				}
				Optional<News> newsO = newsQ.getById(id);
				if(!newsO.isPresent())
				{
					responseMap.put("code", 4);
					return;
				}

				News news = newsO.get();
				if(news.getSchool().getId() != user.getSchool().getId())
				{
					responseMap.put("code", 5);
					return;
				}

				if(title != null && title.length() > 0)
				{
					news.setTitle(title);
				}
				if(content != null && content.length() > 0)
				{
					news.setContent(content);
				}
				if(status != null && Arrays.asList(News.STATUS_ALL).contains(status))
				{
					news.setStatus(status);
				}
				if(lang != null && lang.length() > 0)
				{
					news.setLang(lang);
				}
				if(order != null)
				{
					news.setOrder(order.intValue());
				}
				if(release != null)
				{
					news.setRelease(new Epoch(release));
					if(new Epoch(release.intValue()).before(Epoch.now()))
					{
						news.setRelease(Epoch.now());
					}
				}
				newsQ.update(news);

				responseMap.put("code", 0);
				responseMap.put("news", news);
			}
		}
		else if("DELETE".equals(request.getMethod()))
		{
			if(user.getAdminLevel() < 1)
			{
				responseMap.put("code", 2);
				return;
			}

			if(id <= 0)
			{
				responseMap.put("code", 3);
				return;
			}
			Optional<News> newsO = newsQ.getById(id);
			if(!newsO.isPresent())
			{
				responseMap.put("code", 4);
				return;
			}
			if(newsO.get().getSchool().getId() != user.getSchool().getId())
			{
				responseMap.put("code", 5);
				return;
			}

			newsO.get().setStatus(News.STATUS_DELETED);
			newsQ.update(newsO.get());

			responseMap.put("code", 0);
		}
		*/
		else
		{
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		return SUCCESS;
	}
}