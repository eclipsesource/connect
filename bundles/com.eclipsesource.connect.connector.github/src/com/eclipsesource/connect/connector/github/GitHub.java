/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.connector.github;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedHashMap;

import com.eclipsesource.connect.connector.github.internal.GitHubFactory;
import com.eclipsesource.connect.connector.github.model.GHCommit;
import com.eclipsesource.connect.connector.github.model.GHEmail;
import com.eclipsesource.connect.connector.github.model.GHHook;
import com.eclipsesource.connect.connector.github.model.GHIssue;
import com.eclipsesource.connect.connector.github.model.GHIssueFilter;
import com.eclipsesource.connect.connector.github.model.GHMarkdownConfiguration;
import com.eclipsesource.connect.connector.github.model.GHOrganization;
import com.eclipsesource.connect.connector.github.model.GHRate;
import com.eclipsesource.connect.connector.github.model.GHRepository;
import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter;
import com.eclipsesource.connect.connector.github.model.GHUser;


public class GitHub {

  public static final String API_URL = "https://api.github.com";

  private final GitHubFactory gitHubFactory;

  public GitHub( GitHubParticipant... participants ) {
    this( "", API_URL, participants );
  }

  public GitHub( String accessToken, GitHubParticipant... participants ) {
    this( accessToken, API_URL, participants );
  }

  public GitHub( String accessToken, String baseUrl, GitHubParticipant... participants ) {
    validateArguments( accessToken, baseUrl );
    gitHubFactory = new GitHubFactory( baseUrl, accessToken, participants );
  }

  private void validateArguments( String accessToken, String baseUrl ) {
    checkArgument( accessToken != null, "Access Token must not be null" );
    checkArgument( baseUrl != null, "Base URL must not be null" );
    checkArgument( !baseUrl.trim().isEmpty(), "Base URL must not be empty" );
  }

  public GHUser getUser() {
    return gitHubFactory.get( "/user", GHUser.class ).getEntity();
  }

  public List<GHEmail> getEmails() {
    GHEmail[] emails = gitHubFactory.get( "/user/emails", GHEmail[].class ).getEntity();
    return Arrays.asList( emails );
  }

  public GHRate getRate() {
    return gitHubFactory.get( "/rate_limit", GHRate.class ).getEntity();
  }

  public GitHubResponse<GHOrganization[]> getOrganizations() {
    Map<String, String> parameter = new HashMap<>();
    parameter.put( "per_page", "100" );
    return gitHubFactory.get( "/user/orgs", parameter, GHOrganization[].class );
  }

  public GitHubResponse<GHOrganization> getOrganization( String organizationName ) {
    return gitHubFactory.get( "/orgs/" + organizationName, GHOrganization.class );
  }

  public GitHubResponse<GHRepository> getRepository( String owner, String repoName ) {
    return gitHubFactory.get( "/repos/" + owner + "/" + repoName, GHRepository.class );
  }

  public GitHubResponse<GHRepository[]> getRepositories( GHRepositoryFilter filter, int page ) {
    Map<String, String> parameter = new HashMap<>();
    parameter.putAll( filter.getFilter() );
    parameter.put( "per_page", "15" );
    parameter.put( "page", String.valueOf( page ) );
    return gitHubFactory.get( "/user/repos", parameter, GHRepository[].class );
  }

  public GitHubResponse<GHRepository[]> getRepositories( GHOrganization org, GHRepositoryFilter filter, int page ) {
    Map<String, String> parameter = new HashMap<>();
    parameter.putAll( filter.getFilter() );
    parameter.put( "per_page", "15" );
    parameter.put( "page", String.valueOf( page ) );
    return gitHubFactory.get( "/orgs/" + org.getLogin() + "/repos", parameter, GHRepository[].class );
  }

  public GitHubResponse<GHHook> createWebHook( GHHook hook, GHRepository repository ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/hooks";
    GitHubResponse<GHHook> response = gitHubFactory.post( path, hook, GHHook.class );
    if( response.getEntity().getId() == null ) {
      GHHook[] hooks = gitHubFactory.get( path, GHHook[].class ).getEntity();
      Optional<GHHook> result = Stream.of( hooks ).filter( currentHook -> currentHook.getConfig().getUrl().equals( hook.getConfig().getUrl() ) ).findFirst();
      if( result.isPresent() ) {
        return new GitHubResponse<>( result.get(), new MultivaluedHashMap<>() );
      }
    }
    return response;
  }

  public GitHubResponse<GHHook> getWebHook( GHRepository repository, String hookId ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/hooks/" + hookId;
    GitHubResponse<GHHook> response = gitHubFactory.get( path, GHHook.class );
    return response;
  }

  public GitHubResponse<GHHook> updateWebHook( GHRepository repository, GHHook hook ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/hooks/" + hook.getId();
    return gitHubFactory.patch( path, hook, GHHook.class );
  }

  public void deleteWebHook( GHRepository repository, GHHook hook ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/hooks/" + hook.getId();
    gitHubFactory.delete( path );
  }

  public GitHubResponse<GHIssue[]> getRepositoryIssues( GHRepository repository, GHIssueFilter filter ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/issues";
    return gitHubFactory.get( path, filter.getFilter(), GHIssue[].class );
  }

  public GitHubResponse<GHCommit[]> getRepositoryCommits( GHRepository repository ) {
    String path = "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/commits";
    return gitHubFactory.get( path, GHCommit[].class );
  }

  public GitHubResponse<String> getMarkdown( GHMarkdownConfiguration configuration ) {
    return gitHubFactory.post( "/markdown", configuration, String.class );
  }

}
