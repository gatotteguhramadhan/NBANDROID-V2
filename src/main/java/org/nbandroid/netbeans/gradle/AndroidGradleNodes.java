package org.nbandroid.netbeans.gradle;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import org.nbandroid.netbeans.gradle.ui.DependenciesNode;
import org.nbandroid.netbeans.gradle.ui.GeneratedSourcesNode;
import org.nbandroid.netbeans.gradle.ui.InstrumentTestNode;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.gradle.project.api.event.NbListenerRef;
import org.netbeans.gradle.project.api.nodes.GradleProjectExtensionNodes;
import org.netbeans.gradle.project.api.nodes.SingleNodeFactory;
import org.netbeans.modules.android.project.api.AndroidConstants;
import org.netbeans.modules.android.project.api.AndroidManifestSource;
import org.netbeans.modules.android.project.api.ui.AndroidPackagesNode;
import org.netbeans.modules.android.project.api.ui.AndroidResourceNode;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author radim
 */
public class AndroidGradleNodes implements GradleProjectExtensionNodes {

  private final Project p;

  public AndroidGradleNodes(Project p) {
    this.p = p;
  }
  
  @Override
  public NbListenerRef addNodeChangeListener(Runnable listener) {
    // TODO copied from gradle java support
    if (listener == null) {
      throw new NullPointerException("listener");
    }
    // FIXME: We currently rely on the undocumented fact, that nodes are
    // always reloaded after a model reload.
    return new NbListenerRef() {
      @Override
      public boolean isRegistered() {
        return false;
      }

      @Override
      public void unregister() {
      }
    };
  }

  @Override
  public List<SingleNodeFactory> getNodeFactories() {
    Sources sources = ProjectUtils.getSources(p);
    return Lists.newArrayList(Iterables.concat(
        Iterables.transform(
          Iterables.concat(
            Arrays.asList(sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA)),
            Arrays.asList(sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_RESOURCES))),
          new Function<SourceGroup, SingleNodeFactory>() {

            @Override
            public SingleNodeFactory apply(SourceGroup f) {
              return new GradleNodeFactory(f);
            }
          }),
        Iterables.transform(
          Arrays.asList(sources.getSourceGroups(AndroidConstants.SOURCES_TYPE_ANDROID_RES)),
          new Function<SourceGroup, SingleNodeFactory>() {

            @Override
            public SingleNodeFactory apply(SourceGroup f) {
              return new GradleResNodeFactory(f);
            }
          }),
        Lists.newArrayList(
          new ManifestNodeFactory(),
          new DependenciesNodeFactory(),
          new InstrumentTestNodeFactory(),
          new GeneratedSourcesNodeFactory())));
  }
  
  private class GradleNodeFactory implements SingleNodeFactory {

    private final SourceGroup sg;

    public GradleNodeFactory(SourceGroup sg) {
      this.sg = sg;
    }
    
    @Override
    public Node createNode() {
      return new AndroidPackagesNode(sg, p);
    }
  }
  
  private class GradleResNodeFactory implements SingleNodeFactory {

    private final SourceGroup sg;

    public GradleResNodeFactory(SourceGroup sg) {
      this.sg = sg;
    }
    
    @Override
    public Node createNode() {
      try {
        DataObject dobj = DataObject.find(sg.getRootFolder());
        return new AndroidResourceNode(dobj.getNodeDelegate(), p, sg.getDisplayName());
      } catch (DataObjectNotFoundException ex) {
        Exceptions.printStackTrace(ex);
      }
      return null;
    }
  }
  
  private class InstrumentTestNodeFactory implements SingleNodeFactory {

    public InstrumentTestNodeFactory() {
    }
    
    @Override
    public Node createNode() {
      return new InstrumentTestNode(NbBundle.getMessage(AndroidGradleNodes.class, "LBL_InstrumentTestNode"), p);
    }
  }
  
  private class DependenciesNodeFactory implements SingleNodeFactory {

    public DependenciesNodeFactory() {
    }
    
    @Override
    public Node createNode() {
      return DependenciesNode.createCompileDependenciesNode(
          NbBundle.getMessage(AndroidGradleNodes.class, "LBL_DependenciesNode"), p);
    }
  }
  
  private class GeneratedSourcesNodeFactory implements SingleNodeFactory {

    public GeneratedSourcesNodeFactory() {
    }
    
    @Override
    public Node createNode() {
      return new GeneratedSourcesNode("Generated Sources", p, Lists.newArrayList(
          AndroidConstants.SOURCES_TYPE_GENERATED_JAVA, AndroidConstants.SOURCES_TYPE_GENERATED_RESOURCES));
    }
  }
  
  private class ManifestNodeFactory implements SingleNodeFactory {

    @Override
    public Node createNode() {
      // TODO there can be more manifests
      AndroidManifestSource ams = p.getLookup().lookup(AndroidManifestSource.class);
      FileObject manifestFO = ams != null ? ams.get() : null;
      if (manifestFO != null) {
        try {
          DataObject dobj = DataObject.find(manifestFO);
          return new AndroidResourceNode(dobj.getNodeDelegate(), p, AndroidConstants.ANDROID_MANIFEST_XML);
        } catch (DataObjectNotFoundException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      return null;
      
    }
  }
}